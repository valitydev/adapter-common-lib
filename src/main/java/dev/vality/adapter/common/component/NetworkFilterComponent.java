package dev.vality.adapter.common.component;

import dev.vality.woody.api.flow.WFlow;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NetworkFilterComponent {

    public static final String HEALTH = "/actuator/health";

    public FilterRegistrationBean externalPortRestrictingFilter(int restPort, String restEndpoint) {
        Filter filter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                String servletPath = request.getServletPath();
                if ((request.getLocalPort() == restPort)
                        && !(servletPath.startsWith(restEndpoint) || servletPath.startsWith(HEALTH))) {
                    response.sendError(404, "Unknown address");
                    return;
                }
                filterChain.doFilter(request, response);
            }
        };

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setOrder(-100);
        filterRegistrationBean.setName("httpPortFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    public FilterRegistrationBean woodyFilter(int restPort, String restEndpoint) {
        WFlow woodyFlow = new WFlow();
        Filter filter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                if ((request.getLocalPort() == restPort)
                        && request.getServletPath().startsWith(restEndpoint)) {
                    woodyFlow.createServiceFork(() -> {
                        try {
                            filterChain.doFilter(request, response);
                        } catch (IOException | ServletException e) {
                            sneakyThrow(e);
                        }
                    }).run();
                    return;
                }
                filterChain.doFilter(request, response);
            }

            private <E extends Throwable, T> T sneakyThrow(Throwable t) throws E {
                throw (E) t;
            }
        };

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setOrder(-50);
        filterRegistrationBean.setName("woodyFilter");
        filterRegistrationBean.addUrlPatterns(restEndpoint + "*");
        return filterRegistrationBean;
    }
}