# Общие данные по проекту при написании адаптера

### Оглавление:

- [Структура проекта](#Структура-проекта)
- [Конфигурация](#configuration)
    - [AppConfiguration](#appconfiguration)
    - [HandlerConfiguration](#handlerconfiguration)
    - [ProcessorConfiguration](#processorconfiguration)
- [Конвертеры](#converter)
    - [entry](#entry)
    - [exit](#exit)
    - [request](#request)
- [flow](#flow)
- [handler](#handler)
- [processor](#processor)

### Структура проекта

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── dev
│   │   │       └── vality
│   │   │           └── adapter
│   │   │               └── provider
│   │   │                   ├── AdapterProviderApplication.java
│   │   │                   ├── client - клиент для обращения к провайдеру
│   │   │                   │   ├── ProviderClient.java
│   │   │                   │   ├── ProviderClientException.java
│   │   │                   │   ├── constant
│   │   │                   │   └── model
│   │   │                   ├── configuration - конфигурационные файлы
│   │   │                   │   ├── AppConfiguration.java - основные конфигурации приложения
│   │   │                   │   ├── HandlerConfiguration.java - описание handler-ов
│   │   │                   │   ├── ProcessorConfiguration.java - описание processor-ов, они вкладываются друг в друга
│   │   │                   │   ├── RestTemplateConfiguration.java
│   │   │                   │   ├── TomcatEmbeddedConfiguration.java
│   │   │                   │   └── properties - параметры
│   │   │                   │       ├── IPayProperties.java
│   │   │                   │       └── RestTemplateProperties.java
│   │   │                   ├── converter
│   │   │                   │   ├── entry - конвертация входящих объектов в модель общего входа
│   │   │                   │   │   ├── CtxToEntryModelConverter.java
│   │   │                   │   │   └── RecCtxToEntryModelConverter.java
│   │   │                   │   ├── exit - конвертация исходящих объектов в модель общего выхода
│   │   │                   │   │   ├── ExitModelToProxyResultConverter.java
│   │   │                   │   │   └── ExitModelToRecTokenProxyResultConverter.java
│   │   │                   │   └── request - конвертация из модели общего входа в модели запросов к провайдеру
│   │   │                   │       ├── EntryStateToCancelRequestConverter.java
│   │   │                   │       ├── EntryStateToCaptureHandler.java
│   │   │                   │       ├── EntryStateToRefundRequestConverter.java
│   │   │                   │       ├── EntryStateToRegisterPreAuthRequestConverter.java
│   │   │                   │       ├── EntryStateToStatusRequestConverter.java
│   │   │                   ├── flow
│   │   │                   │   └── StepResolverImpl.java - определяет, какой шаг будет на входе и на выходе
│   │   │                   ├── handler - Обработчики шагов. Выполняют роль конвертации общей модели в запрос и его дальнейшее выполнение с обработкой полученного ответа
│   │   │                   │   ├── CancelHandler.java
│   │   │                   │   ├── CapturedHandler.java
│   │   │                   │   ├── PaymentOrderHandler.java
│   │   │                   │   ├── RefundHandler.java
│   │   │                   │   ├── RegisterPreAuthHandler.java
│   │   │                   │   ├── StatusHandler.java
│   │   │                   │   └── StepHandler.java
│   │   │                   ├── processor - процессоры отвечают за подготовку к общей модели выхода, описываются в ProcessorConfiguration
│   │   │                   │   ├── CommonErrorProcessor.java
│   │   │                   │   └── CommonProcessor.java
│   │   │                   ├── service
│   │   │                   │   └── AdapterIPayServerHandler.java - в нем описывается общая логика имплементирующая интерфейс для работы с провайдерами
│   │   │                   ├── servlet
│   │   │                   │   └── IPayAdapterServlet.java
│   │   │                   └── validator - необходимые проверки, например, что в опциях пришли ожидаемые параметры
│   │   │                       ├── PaymentContextValidator.java
│   │   │                       └── RecurrentTokenContextValidator.java
│   │   └── resources
│   │       ├── application.yml
│   │       └── fixture
│   │           └── errors.json - маппинг ошибок
```

### configuration

#### AppConfiguration

основные конфигурации приложения, там находятся общие `beans`, например:

```
    @Bean
    @ConfigurationProperties("time.config")
    public CommonTimerProperties commonTimerProperties() {
        return new CommonTimerProperties();
    }

    @Bean
    public ExponentialBackOffPollingService<PollingInfo> exponentialBackOffPollingService() {
        return new ExponentialBackOffPollingService<>();
    }

    @Bean
    public PaymentCallbackHandler paymentCallbackHandler(
            AdapterDeserializer adapterDeserializer,
            AdapterSerializer adapterSerializer,
            CallbackDeserializer callbackDeserializer
    ) {
        return new PaymentCallbackHandler(adapterDeserializer, adapterSerializer, callbackDeserializer);
    }
```

#### HandlerConfiguration

Конфигурация каждого описанного раннее обработчика. В примере ниже описан обработчик для шага PRE_AUTH

```
@Configuration
@RequiredArgsConstructor
public class HandlerConfiguration {

    private final ProviderClient providerClient;

    private final EntryStateToRegisterPreAuthRequestConverter entryStateToRegisterPreAuthRequestConverter;

    @Bean
    public RegisterPreAuthHandler registerPreAuthHandler(Processor<GeneralExitStateModel, Response, GeneralEntryStateModel> processor) {
        return new RegisterPreAuthHandler(providerClient, entryStateToRegisterPreAuthRequestConverter, processor);
    }

```

#### ProcessorConfiguration

Описывается в цепочку вызовов, матрешечный метод

```
@Configuration
public class ProcessorConfiguration {
    @Bean
    public Processor<GeneralExitStateModel, Response, GeneralEntryStateModel> commonErrorProcessor(ProviderClient providerClient) {
        Processor<GeneralExitStateModel, Response, GeneralEntryStateModel> commonProcessor = new CommonProcessor(providerClient);
        return new CommonErrorProcessor(commonProcessor);
    }
}
```

### Converter

#### entry

Содержит в себе классы, которые конвертируют входящие модели и структуры в общую модель входа GeneralEntryStateModel с
которой далее и работаем.

```

@Component
@RequiredArgsConstructor
public class CtxToEntryModelConverter implements Converter<PaymentContext, GeneralEntryStateModel> {

    private final CdsClientStorage cdsStorage;
    private final AdapterDeserializer adapterDeserializer;

    @Override
    public GeneralEntryStateModel convert(PaymentContext context) {
        // any code
        return GeneralEntryStateModel.builder()
                .trxId(payment.getTrx() != null ? payment.getTrx().getId() : adapterContext.getTrxId())
                .trxExtra(payment.getTrx() != null ? payment.getTrx().getExtra() : new HashMap<>())
                .orderId(orderId)
                .amount(cash.getAmount())
                .refundAmount(cash.getAmount())
                .currencySymbolCode(payment.getCost().getCurrency().getSymbolicCode())
                .currencyCode(payment.getCost().getCurrency().getNumericCode())
                .createdAt(createdAt)
                .pan(cardData != null ? cardData.getPan() : null)
                .cvv2(CardDataUtils.extractCvv2(sessionData))
                .expYear(cardData != null ? cardData.getExpYear() : null)
                .expMonth(cardData != null ? cardData.getExpMonth() : null)
                .cardHolder(cardData != null ? cardData.getCardholderName() : null)
                .email(PaymentDataConverter.extractEmail(context.getPaymentInfo()))
                .options(paymentContext.getOptions())
                .makeRecurrent(payment.make_recurrent)
                .callbackUrl(redirectUrl)
                .ip(ipAddress)
                .trxExtra(extra)
                .adapterContext(adapterContext)
                .targetStatus(targetStatus)
                .build();
    }

}

```

#### exit

Содержит в себе классы, которые конвертируют из общей модели выхода в PaymentProxyResult, в них производится анализ на
основании шага (step), какой intent подготовить

```
@Slf4j
@Component
@RequiredArgsConstructor
public class ExitModelToProxyResultConverter implements Converter<GeneralExitStateModel, PaymentProxyResult> {

    private final CommonTimerProperties timerProperties;
    private final ErrorMapping errorMapping;
    private final AdapterSerializer adapterSerializer;
    private final ExponentialBackOffPollingService<PollingInfo> exponentialBackOffPollingService;

    @Override
    public PaymentProxyResult convert(GeneralExitStateModel exitStateModel) {
        log.info("PaymentProxy exit state model: {}", exitStateModel);
        if (!StringUtils.isEmpty(exitStateModel.getErrorCode())) {
            log.info("Exit error code={}, message={}", exitStateModel.getErrorCode(), exitStateModel.getErrorMessage());
            Failure failure = errorMapping.mapFailure(exitStateModel.getErrorCode());
            return createProxyResultFailure(failure);
        }

        AdapterContext adapterContext = exitStateModel.getAdapterContext();
        Step nextStep = exitStateModel.getAdapterContext().getStep();

        Intent intent;
        switch (nextStep) {
            case AUTH:
                intent = createIntentWithSleepIntent(0);
                break;
            case FINISH_THREE_DS:
                intent = createIntentWithSuspendIntent(exitStateModel);
                break;
            case CHECK_STATUS:
            case CAPTURE:
            case REFUND:
            case CANCEL:
            case DO_NOTHING:
                intent = createFinishIntentSuccess();
                break;
            default:
                throw new IllegalStateException("Wrong next step: " + nextStep);
        }

        PaymentProxyResult paymentProxyResult = new PaymentProxyResult(intent).setNextState(adapterSerializer.writeByte(adapterContext));
        paymentProxyResult.setTrx(createTransactionInfo(adapterContext.getTrxId(), exitStateModel.getTrxExtra()));
        log.info("Payment proxy result: {}", paymentProxyResult);
        return paymentProxyResult;
    }

}
```

#### request

Содержит классы, которые из общей модели заполняют структуру для подготовки запроса к провайдеру

Например:

```
@Component
public class EntryStateToCancelRequestConverter implements Converter<GeneralEntryStateModel, Request> {

    @Override
    public Request convert(GeneralEntryStateModel entryStateModel) {
        Map<String, String> options = entryStateModel.getOptions();
        String userName = options.get(OptionField.LOGIN.getField());
        String password = options.get(OptionField.PASSWORD.getField());

        return Request.builder()
                .userName(userName)
                .password(password)
                .orderId(entryStateModel.getTrxId())
                .build();
    }
}

```

## flow

StepResolverImpl - определяет шаг в самом начале и устанавливает шаг на выходе

```
@Slf4j
@Component
public class StepResolverImpl implements StepResolver<GeneralEntryStateModel, GeneralExitStateModel> {

    @Override
    public Step resolveEntry(GeneralEntryStateModel entryStateModel) {
        TargetStatus targetStatus = entryStateModel.getTargetStatus();
        AdapterContext adapterContext = entryStateModel.getAdapterContext();
        Step step = adapterContext.getStep();

        if (targetStatus == null) {
            return Objects.requireNonNullElse(step, Step.PRE_AUTH);
        }

        log.info("Entry resolver. Target status: {}. Step: {}", targetStatus, step);
        switch (targetStatus) {
            case PROCESSED:
                return Objects.requireNonNullElse(step, Step.PRE_AUTH);
            case CAPTURED:
                return Step.CAPTURE;
            case CANCELLED:
                return Step.CANCEL;
            case REFUNDED:
                return Step.REFUND;
            default:
                throw new IllegalStateException("Unknown target status: " + targetStatus);
        }
    }

    @Override
    public Step resolveExit(GeneralExitStateModel exitStateModel) {
        GeneralEntryStateModel entryStateModel = exitStateModel.getGeneralEntryStateModel();
        Step step = entryStateModel.getAdapterContext().getStep();
        log.info("Exit resolver. Step: {}", step);
        switch (step) {
            case PRE_AUTH:
                return Step.AUTH;
            case AUTH:
                return Step.FINISH_THREE_DS;
            case CAPTURE:
                return Step.CAPTURE;
            case FINISH_THREE_DS:
                return Step.CHECK_STATUS;
            case CHECK_STATUS:
                return Step.DO_NOTHING;
            default:
                return step;
        }
    }

}
```

#### handler

Обработчики шагов.

Выполняют роль конвертации общей модели в запрос и его дальнейшее выполнение с обработкой полученного ответа Handler'ы
описываются в CommonHandler, то есть если не использовать CommonHandler, можно и не использовать processor и converter

В примере ниже видно, что данный класс будет использован, если удовлетворит условию `step == Step.CANCEL`, т.е. в данном
случае будет выбран обработчик для вызова отмены платежа, после того, как будет известен обработчик, у него будет вызван
метод handle(GeneralEntryStateModel entryStateModel) находящийся в `StepHandler`

Передаем в `StepHandler<Request, Response>`(который имплементирует CommonHandler) объекты, которые ожидаем в качестве
запроса и ответа от провайдера

Видно, что в качестве объекта для запроса используется класс Request, в качестве ожидаемого ответа Response

Для обращения к провайдеру используется клиент `ProviderClient` с вызовом метода `reverse`.

Конвертор заполнит модель Request данными из модели GeneralEntryStateModel

Процессор на основании полученного `Response` и `GeneralEntryStateModel` заполнит `GeneralExitStateModel`

```
public class CancelHandler extends StepHandler<Request, Response> {

    public CancelHandler(
            ProviderClient providerClient,
            Converter<GeneralEntryStateModel, Request> converter,
            Processor<GeneralExitStateModel, Response, GeneralEntryStateModel> processor
    ) {
        super(providerClient::reverse, converter, processor);
    }

    @Override
    public boolean isHandle(GeneralEntryStateModel entryStateModel) {
        Step step = entryStateModel.getAdapterContext().getStep();
        return step == Step.CANCEL;
    }
}

```

ps важно помнить, что после добавлении нового handler-а, нужно его прописать
в [HandlerConfiguration](#handlerconfiguration), но до этого, придется еще
написать [entry](#entry) [converter-ы](#converter) для [handler-а](#handler), описать [processor](#processor) и модель
ответа, нашем случае это Response

#### Processor

Процессоры отвечают за подготовку к общей модели выхода (GeneralExitStateModel), описываются в папке `processor`, а
матрешочное поведение описывается в конфигурации `ProcessorConfiguration`.

Важно помнить, что после создания процессора, его необходимо добавить в [конфигурацию](#processorconfiguration).

Пример processor-а:

```
@Slf4j
@RequiredArgsConstructor
public class CommonErrorProcessor implements Processor<GeneralExitStateModel, Response, GeneralEntryStateModel> {

    private final Processor<GeneralExitStateModel, Response, GeneralEntryStateModel> nextProcessor;

    @Override
    public GeneralExitStateModel process(Response response, GeneralEntryStateModel entryStateModel) {
        log.info("Order response: {}", response);
        AdapterContext adapterContext = entryStateModel.getAdapterContext();

        if(response.getErrorCode() != null) {
            ErrorCode errorCode = ErrorCode.findByCode(response.getErrorCode());
            return GeneralExitStateModel.builder()
                    .errorCode(response.getErrorCode())
                    .errorMessage(errorCode.getDescription())
                    .adapterContext(adapterContext)
                    .generalEntryStateModel(entryStateModel)
                    .trxExtra(new HashMap<>(entryStateModel.getTrxExtra()))
                    .build();
        } else if (nextProcessor != null) {
            return nextProcessor.process(response, entryStateModel);
        }
        return null;
    }
}
```

Таким образом видно, что, если условие с ошибкой не отработало

```
if(response.getErrorCode() != null)
``` 

то, будет вызван следующий processor

```
return nextProcessor.process(response, entryStateModel);
```

Что, собственно, и описывается в конфигурации, см. раздел [конфигурации](#processorconfiguration)
