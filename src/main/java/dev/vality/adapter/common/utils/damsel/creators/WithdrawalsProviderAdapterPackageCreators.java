package dev.vality.adapter.common.utils.damsel.creators;

import dev.vality.damsel.base.Timer;
import dev.vality.damsel.domain.Failure;
import dev.vality.damsel.domain.TransactionInfo;
import dev.vality.damsel.msgpack.Value;
import dev.vality.damsel.withdrawals.provider_adapter.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WithdrawalsProviderAdapterPackageCreators {

    public static Intent createIntentWithSleepIntent(Integer timer) {
        return Intent.sleep(createSleepIntent(BasePackageCreators.createTimerTimeout(timer)));
    }

    public static SleepIntent createSleepIntent(Timer timer) {
        return new SleepIntent(timer);
    }

    // ProxyResult
    public static ProcessResult createProcessResult(Intent intent, Value nextState, TransactionInfo transactionInfo) {
        return new ProcessResult(intent).setNextState(nextState).setTrx(transactionInfo);
    }

    public static ProcessResult createProcessResult(Intent intent) {
        return WithdrawalsProviderAdapterPackageCreators.createProcessResult(intent, null, null);
    }

    public static ProcessResult createProcessResult(Intent intent, Value nextStat) {
        return WithdrawalsProviderAdapterPackageCreators.createProcessResult(intent, nextStat, null);
    }

    public static ProcessResult createProcessResult(Intent intent, TransactionInfo transactionInfo) {
        return WithdrawalsProviderAdapterPackageCreators.createProcessResult(intent, null, transactionInfo);
    }

    // FinishIntent
    @Deprecated
    public static Intent createFinishIntentSuccess(TransactionInfo transactionInfo) {
        return Intent.finish(new FinishIntent(createFinishStatusSuccess(transactionInfo)));
    }

    @Deprecated
    public static FinishStatus createFinishStatusSuccess(TransactionInfo transactionInfo) {
        Success success = new Success();
        success.setTrxInfo(transactionInfo);
        return FinishStatus.success(success);
    }

    public static FinishStatus createFinishStatusSuccess() {
        return FinishStatus.success(new Success());
    }

    public static Intent createFinishIntentFailure(Failure failure) {
        return Intent.finish(new FinishIntent(createFinishStatusFailure(failure)));
    }

    public static ProcessResult createProcessResultFailure(Failure failure) {
        return createProcessResult(createFinishIntentFailure(failure));
    }

    public static ProcessResult createProcessResultFailure(Failure failure, TransactionInfo transactionInfo) {
        return createProcessResult(createFinishIntentFailure(failure), transactionInfo);
    }

    public static FinishStatus createFinishStatusFailure(Failure failure) {
        return FinishStatus.failure(failure);
    }

}
