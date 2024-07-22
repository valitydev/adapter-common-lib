package dev.vality.adapter.common.damsel;

import dev.vality.damsel.base.Timer;
import dev.vality.damsel.domain.*;
import dev.vality.damsel.proxy_provider.Cash;
import dev.vality.damsel.proxy_provider.Invoice;
import dev.vality.damsel.proxy_provider.InvoicePayment;
import dev.vality.damsel.proxy_provider.InvoicePaymentRefund;
import dev.vality.damsel.proxy_provider.Shop;
import dev.vality.damsel.proxy_provider.*;
import dev.vality.damsel.timeout_behaviour.TimeoutBehaviour;
import dev.vality.damsel.user_interaction.BrowserGetRequest;
import dev.vality.damsel.user_interaction.BrowserHTTPRequest;
import dev.vality.damsel.user_interaction.BrowserPostRequest;
import dev.vality.damsel.user_interaction.UserInteraction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static dev.vality.adapter.common.damsel.BasePackageCreators.createTimerTimeout;
import static dev.vality.adapter.common.damsel.ProxyProviderPackageExtractors.extractInvoiceId;
import static dev.vality.adapter.common.damsel.ProxyProviderPackageExtractors.extractPaymentId;
import static dev.vality.adapter.common.damsel.model.Error.DEFAULT_ERROR_CODE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProxyProviderPackageCreators {

    public static final String DEFAULT_IP_ADDRESS = "0.0.0.0";
    public static final String INVOICE_PAYMENT_SEPARATOR_POINT = ".";

    public static String createInvoiceWithPayment(PaymentInfo paymentInfo, String separator) {
        return extractInvoiceId(paymentInfo) + separator + extractPaymentId(paymentInfo);
    }

    public static String createInvoiceWithPayment(PaymentInfo paymentInfo) {
        return createInvoiceWithPayment(paymentInfo, INVOICE_PAYMENT_SEPARATOR_POINT);
    }

    // RecurrentTokenIntent
    public static RecurrentTokenSuccess createRecurrentTokenSuccess(String token) {
        return new RecurrentTokenSuccess(token);
    }

    public static RecurrentTokenFinishIntent createRecurrentTokenStatusSuccess(String token) {
        return new RecurrentTokenFinishIntent(RecurrentTokenFinishStatus.success(createRecurrentTokenSuccess(token)));
    }

    public static RecurrentTokenFinishIntent createRecurrentTokenStatusFailure(Failure failure) {
        return new RecurrentTokenFinishIntent(RecurrentTokenFinishStatus.failure(failure));
    }

    public static RecurrentTokenIntent createRecurrentTokenFinishIntentFailure(Failure failure) {
        return RecurrentTokenIntent.finish(createRecurrentTokenStatusFailure(failure));
    }

    public static RecurrentTokenIntent createRecurrentTokenFinishIntentSuccess(String token) {
        return RecurrentTokenIntent.finish(createRecurrentTokenStatusSuccess(token));
    }

    public static RecurrentTokenIntent createRecurrentTokenWithSuspendIntent(String tag,
                                                                             int timer,
                                                                             UserInteraction userInteraction) {
        return RecurrentTokenIntent.suspend(createSuspendIntent(tag, timer, userInteraction));
    }

    public static RecurrentTokenIntent createRecurrentTokenWithSuspendIntent(String tag, int timer) {
        return createRecurrentTokenWithSuspendIntent(tag, timer, null);
    }

    // RecurrentTokenInfo
    public static RecurrentTokenInfo createRecurrentTokenInfo(RecurrentPaymentTool recurrentPaymentTool) {
        return new RecurrentTokenInfo()
                .setPaymentTool(recurrentPaymentTool);
    }

    // DisposablePaymentResource
    public static DisposablePaymentResource createDisposablePaymentResource(String sessionId,
                                                                            PaymentTool paymentTool) {
        return new DisposablePaymentResource().setPaymentSessionId(sessionId).setPaymentTool(paymentTool);
    }

    // RecurrentPaymentTool
    public static RecurrentPaymentTool createRecurrentPaymentTool(DisposablePaymentResource paymentResource) {
        return new RecurrentPaymentTool().setPaymentResource(paymentResource);
    }

    public static RecurrentPaymentTool createRecurrentPaymentTool(DisposablePaymentResource disposablePaymentResource,
                                                                  Cash cash) {
        return new RecurrentPaymentTool().setPaymentResource(disposablePaymentResource).setMinimalPaymentCost(cash);
    }

    public static RecurrentPaymentTool createRecurrentPaymentTool(String id,
                                                                  DisposablePaymentResource disposablePaymentResource,
                                                                  Cash cash) {
        return new RecurrentPaymentTool()
                .setPaymentResource(disposablePaymentResource)
                .setMinimalPaymentCost(cash)
                .setId(id)
                .setCreatedAt(Instant.now().toString());
    }

    // RecurrentTokenProxyResult
    public static RecurrentTokenProxyResult createRecurrentTokenProxyResult(RecurrentTokenIntent intent,
                                                                            byte[] nextState,
                                                                            TransactionInfo trx) {
        return new RecurrentTokenProxyResult(intent).setNextState(nextState).setTrx(trx);
    }

    public static RecurrentTokenProxyResult createRecurrentTokenProxyResult(RecurrentTokenIntent intent) {
        return createRecurrentTokenProxyResult(intent, null, null);
    }

    public static RecurrentTokenProxyResult createRecurrentTokenProxyResult(RecurrentTokenIntent intent,
                                                                            byte[] nextState) {
        return createRecurrentTokenProxyResult(intent, nextState, null);
    }

    public static RecurrentTokenProxyResult createRecurrentTokenProxyResultFailure(Failure failure) {
        return createRecurrentTokenProxyResult(createRecurrentTokenFinishIntentFailure(failure));
    }

    // ProxyResult
    public static PaymentProxyResult createPaymentProxyResult(Intent intent, byte[] nextState, TransactionInfo trx) {
        return new PaymentProxyResult(intent).setNextState(nextState).setTrx(trx);
    }

    public static PaymentProxyResult createPaymentProxyResult(Intent intent, byte[] nextState) {
        return createPaymentProxyResult(intent, nextState, null);
    }

    public static PaymentProxyResult createPaymentProxyResult(Intent intent) {
        return createPaymentProxyResult(intent, null, null);
    }

    public static PaymentProxyResult createProxyResultFailure(Failure failure) {
        return new PaymentProxyResult(createFinishIntentFailure(failure));
    }

    public static PaymentInfo createPaymentInfo(Invoice invoice, Shop shop, InvoicePayment invoicePayment) {
        return createPaymentInfo(invoice, shop, invoicePayment, null);
    }

    public static PaymentInfo createPaymentInfo(Invoice invoice,
                                                Shop shop,
                                                InvoicePayment invoicePayment,
                                                InvoicePaymentRefund invoicePaymentRefund) {
        return new PaymentInfo(shop, invoice, invoicePayment).setRefund(invoicePaymentRefund);
    }

    public static PaymentContext createContext(PaymentInfo paymentInfo, Session session, Map<String, String> options) {
        return new PaymentContext(session, paymentInfo).setOptions(options);
    }

    public static PaymentResource createPaymentResourceDisposablePaymentResource(
            DisposablePaymentResource disposablePaymentResource
    ) {
        PaymentResource paymentResource = new PaymentResource();
        paymentResource.setDisposablePaymentResource(disposablePaymentResource);
        return paymentResource;
    }

    public static RecurrentPaymentResource createRecurrentPaymentResource(String token) {
        return new RecurrentPaymentResource().setRecToken(token);
    }

    public static PaymentResource createPaymentResourceRecurrentPaymentResource(
            RecurrentPaymentResource recurrentPaymentResource
    ) {
        return PaymentResource.recurrent_payment_resource(recurrentPaymentResource);
    }

    public static InvoicePayment createInvoicePaymentWithTrX(String invoicePaymentId,
                                                             String createdAt,
                                                             PaymentResource paymentResource,
                                                             Cash cost,
                                                             TransactionInfo transactionInfo) {
        return createInvoicePaymentWithTrX(
                invoicePaymentId, createdAt, paymentResource, cost, transactionInfo, null
        );
    }

    public static InvoicePayment createInvoicePaymentWithTrX(String invoicePaymentId,
                                                             String createdAt,
                                                             PaymentResource paymentResource,
                                                             Cash cost,
                                                             TransactionInfo transactionInfo,
                                                             ContactInfo contactInfo) {
        return new InvoicePayment()
                .setId(invoicePaymentId)
                .setCreatedAt(createdAt)
                .setPaymentResource(paymentResource)
                .setCost(cost)
                .setTrx(transactionInfo)
                .setContactInfo(contactInfo);
    }

    public static Invoice createInvoice(String invoicePaymentId,
                                        String createdAt,
                                        Cash cost) {
        return new Invoice().setId(invoicePaymentId).setCreatedAt(createdAt).setCost(cost);
    }

    public static InvoicePaymentRefund createInvoicePaymentRefund(String refundId,
                                                                  TransactionInfo trx,
                                                                  Cash cash) {
        return new InvoicePaymentRefund()
                .setId(refundId)
                .setTrx(trx)
                .setCash(cash);
    }

    public static Session createSession(TargetInvoicePaymentStatus target, byte[] state) {
        return new Session(target).setState(state);
    }

    public static Session createSession(TargetInvoicePaymentStatus target) {
        return createSession(target, null);
    }

    public static Session createSession(byte[] state) {
        return new Session().setState(state);
    }

    public static Session createSession(ByteBuffer state) {
        return new Session().setState(state);
    }

    public static PaymentCallbackProxyResult createCallbackProxyResult(Intent intent,
                                                                       byte[] nextState,
                                                                       TransactionInfo trx) {
        return new PaymentCallbackProxyResult().setIntent(intent).setNextState(nextState).setTrx(trx);
    }

    public static PaymentCallbackProxyResult createCallbackProxyResultFailure(Failure failure) {
        return new PaymentCallbackProxyResult().setIntent(createFinishIntentFailure(failure));
    }

    public static PaymentCallbackResult createCallbackResult(byte[] callbackResponse,
                                                             PaymentCallbackProxyResult proxyResult) {
        return new PaymentCallbackResult().setResponse(callbackResponse).setResult(proxyResult);
    }

    public static PaymentCallbackResult createCallbackResultFailure(byte[] callbackResponse, Failure failure) {
        return new PaymentCallbackResult()
                .setResponse(callbackResponse)
                .setResult(createCallbackProxyResultFailure(failure));
    }

    public static PaymentCallbackResult createCallbackResultFailure(Failure failure) {
        return createCallbackResultFailure(DEFAULT_ERROR_CODE.getBytes(), failure);
    }

    // RecurrentTokenCallbackResult
    public static RecurrentTokenCallbackResult createRecurrentTokenCallbackResult(
            byte[] callbackResponse,
            RecurrentTokenProxyResult proxyResult
    ) {
        return new RecurrentTokenCallbackResult().setResponse(callbackResponse).setResult(proxyResult);
    }

    public static RecurrentTokenCallbackResult createRecurrentTokenCallbackResultFailure(byte[] callbackResponse,
                                                                                         Failure failure) {
        return new RecurrentTokenCallbackResult()
                .setResponse(callbackResponse)
                .setResult(createRecurrentTokenProxyResult(createRecurrentTokenFinishIntentFailure(failure)));
    }

    public static RecurrentTokenCallbackResult createRecurrentTokenCallbackResultFailure(Failure failure) {
        return createRecurrentTokenCallbackResultFailure(DEFAULT_ERROR_CODE.getBytes(), failure);
    }

    // FinishIntent
    public static Intent createFinishIntentSuccess() {
        return Intent.finish(new FinishIntent(createFinishStatusSuccess()));
    }

    public static Intent createFinishIntentSuccessWithToken(String token) {
        return Intent.finish(new FinishIntent(createFinishStatusSuccess(token)));
    }

    public static Intent createFinishIntentFailure(String code, String description) {
        return Intent.finish(new FinishIntent(createFinishStatusFailure(
                DomainPackageCreators.createFailure(code, description))));
    }

    public static Intent createFinishIntentFailure(Failure failure) {
        return Intent.finish(new FinishIntent(createFinishStatusFailure(failure)));
    }

    public static Intent createIntentWithSuspendIntent(String tag, Integer timer, UserInteraction userInteraction) {
        return Intent.suspend(createSuspendIntent(tag, timer, userInteraction));
    }

    public static Intent createIntentWithSuspendIntent(String tag, Integer timer) {
        return createIntentWithSuspendIntent(tag, timer, null);
    }

    public static SuspendIntent createSuspendIntent(String tag, Integer timer, UserInteraction userInteraction) {
        return new SuspendIntent(tag, createTimerTimeout(timer)).setUserInteraction(userInteraction);
    }

    public static SuspendIntent createSuspendIntent(String tag,
                                                    Integer timer,
                                                    UserInteraction userInteraction,
                                                    TimeoutBehaviour timeoutBehaviour) {
        return new SuspendIntent(tag, createTimerTimeout(timer))
                .setUserInteraction(userInteraction)
                .setTimeoutBehaviour(timeoutBehaviour);
    }

    public static SuspendIntent createSuspendIntentTimeoutBehaviourWithFailure(String tag,
                                                                               Integer timer,
                                                                               UserInteraction userInteraction,
                                                                               Failure failure) {
        return new SuspendIntent(tag, createTimerTimeout(timer))
                .setUserInteraction(userInteraction)
                .setTimeoutBehaviour(createTimeoutBehaviourWithFailure(failure));
    }

    public static TimeoutBehaviour createTimeoutBehaviour(OperationFailure operationFailure) {
        return TimeoutBehaviour.operation_failure(operationFailure);
    }

    public static TimeoutBehaviour createTimeoutBehaviourWithFailure(Failure failure) {
        return TimeoutBehaviour.operation_failure(OperationFailure.failure(failure));
    }

    public static TimeoutBehaviour createTimeoutBehaviourWithOperationTimeout() {
        return TimeoutBehaviour.operation_failure(OperationFailure.operation_timeout(new OperationTimeout()));
    }

    public static Intent createIntentWithSleepIntent(Integer timer) {
        return Intent.sleep(createSleepIntent(createTimerTimeout(timer)));
    }

    public static Intent createIntentWithSleepIntent(Integer timer, UserInteraction userInteraction) {
        return Intent.sleep(createSleepIntent(timer, userInteraction));
    }

    public static SleepIntent createSleepIntent(Timer timer) {
        return new SleepIntent(timer);
    }

    public static SleepIntent createSleepIntent(Integer timer, UserInteraction userInteraction) {
        return createSleepIntent(createTimerTimeout(timer)).setUserInteraction(userInteraction);
    }

    public static FinishStatus createFinishStatusFailure(Failure failure) {
        return FinishStatus.failure(failure);
    }

    public static FinishStatus createFinishStatusSuccess() {
        return FinishStatus.success(new Success());
    }

    public static FinishStatus createFinishStatusSuccess(String token) {
        return FinishStatus.success(new Success().setToken(token));
    }

    public static UserInteraction createPostUserInteraction(String url, Map<String, String> form) {
        return createUserInteraction(createBrowserPostRequest(url, form));
    }

    public static UserInteraction createGetUserInteraction(String url) {
        return createUserInteraction(createBrowserGetRequest(url));
    }

    public static UserInteraction createUserInteraction(BrowserHTTPRequest browserHttpRequest) {
        return UserInteraction.redirect(browserHttpRequest);
    }

    public static BrowserHTTPRequest createBrowserPostRequest(String url, Map<String, String> form) {
        return BrowserHTTPRequest.post_request(new BrowserPostRequest(url, form));
    }

    public static BrowserHTTPRequest createBrowserGetRequest(String url) {
        return BrowserHTTPRequest.get_request(new BrowserGetRequest(url));
    }

    @Deprecated
    public static BankCard createBankCardWithToken(BankCard bankCard,
                                                   LegacyBankCardTokenProvider bankCardTokenProvider) {
        bankCard.setTokenProviderDeprecated(bankCardTokenProvider);
        return bankCard;
    }

    public static TransactionInfo extractTransactionInfo(PaymentContext context) {
        return context.getPaymentInfo().getPayment().getTrx();
    }

    public static RecurrentTokenInfo extractTransactionInfo(RecurrentTokenContext context) {
        return context.getTokenInfo();
    }

    public static byte[] extractSessionState(PaymentContext context) {
        return context.getSession().getState();
    }

    public static byte[] extractSessionState(RecurrentTokenContext context) {
        return context.getSession().getState();
    }

    public static String extractIpAddress(DisposablePaymentResource disposablePaymentResource) {
        return Optional.ofNullable(disposablePaymentResource)
                .map(DisposablePaymentResource::getClientInfo)
                .map(ClientInfo::getIpAddress).orElse(DEFAULT_IP_ADDRESS);
    }

    public static String extractIpAddress(PaymentContext context) {
        return extractIpAddress(extractDisposablePaymentResource(context));
    }

    public static String extractIpAddress(RecurrentTokenContext context) {
        return extractIpAddress(extractDisposablePaymentResource(context));
    }

    public static DisposablePaymentResource extractDisposablePaymentResource(RecurrentTokenContext context) {
        Optional<RecurrentPaymentTool> paymentTool = Optional.of(context).map(RecurrentTokenContext::getTokenInfo)
                .map(RecurrentTokenInfo::getPaymentTool);

        if (paymentTool.isPresent() && paymentTool.get().isSetPaymentResource()) {
            paymentTool.get().getPaymentResource();
        }

        return null;
    }

    public static DisposablePaymentResource extractDisposablePaymentResource(PaymentContext context) {
        Optional<PaymentResource> paymentResource = Optional.of(context)
                .map(PaymentContext::getPaymentInfo)
                .map(PaymentInfo::getPayment)
                .map(InvoicePayment::getPaymentResource);

        if (paymentResource.isPresent() && paymentResource.get().isSetDisposablePaymentResource()) {
            return paymentResource.get().getDisposablePaymentResource();
        }

        return null;
    }

}
