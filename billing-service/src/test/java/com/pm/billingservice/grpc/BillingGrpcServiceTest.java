package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.repository.BillingAccountRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingGrpcServiceTest {

    @Mock
    private BillingAccountRepository billingAccountRepository;

    @Mock
    private StreamObserver<BillingResponse> responseObserver;

    @Test
    void createBillingAccountSavesAccountAndReturnsResponse() {
        BillingGrpcService service = new BillingGrpcService(billingAccountRepository);
        BillingRequest request = BillingRequest.newBuilder()
                .setUserId("user-1")
                .setName("Alex")
                .setEmail("alex@example.com")
                .build();

        BillingAccount saved = new BillingAccount();
        saved.setAccountId("acct-1");
        saved.setUserId("user-1");
        saved.setName("Alex");
        saved.setEmail("alex@example.com");
        saved.setStatus("ACTIVE");

        when(billingAccountRepository.save(any(BillingAccount.class))).thenReturn(saved);

        service.createBillingAccount(request, responseObserver);

        ArgumentCaptor<BillingResponse> responseCaptor = ArgumentCaptor.forClass(BillingResponse.class);
        verify(responseObserver).onNext(responseCaptor.capture());
        verify(responseObserver).onCompleted();

        BillingResponse response = responseCaptor.getValue();
        assertThat(response.getAccountId()).isEqualTo("acct-1");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void createBillingAccountReturnsInternalOnRepositoryFailure() {
        BillingGrpcService service = new BillingGrpcService(billingAccountRepository);
        BillingRequest request = BillingRequest.newBuilder()
                .setUserId("user-2")
                .setName("Jamie")
                .setEmail("jamie@example.com")
                .build();

        when(billingAccountRepository.save(any(BillingAccount.class)))
                .thenThrow(new RuntimeException("db down"));

        service.createBillingAccount(request, responseObserver);

        ArgumentCaptor<Throwable> errorCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(responseObserver).onError(errorCaptor.capture());
        Throwable error = errorCaptor.getValue();
        assertThat(error).isInstanceOf(StatusRuntimeException.class);
        StatusRuntimeException statusEx = (StatusRuntimeException) error;
        assertThat(statusEx.getStatus().getCode()).isEqualTo(Status.Code.INTERNAL);
    }
}
