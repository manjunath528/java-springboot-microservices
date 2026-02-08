package com.pm.userservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingServiceGrpcClientTest {

    @Mock
    private BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    @Test
    void createBillingAccountDelegatesToStub() {
        BillingServiceGrpcClient client = new BillingServiceGrpcClient("localhost", 1);
        ReflectionTestUtils.setField(client, "blockingStub", blockingStub);

        BillingResponse expected = BillingResponse.newBuilder()
                .setAccountId("acct-1")
                .setStatus("ACTIVE")
                .build();
        when(blockingStub.createBillingAccount(any(BillingRequest.class))).thenReturn(expected);

        BillingResponse response = client.createBillingAccount("user-1", "Alex", "alex@example.com");

        assertThat(response.getAccountId()).isEqualTo("acct-1");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
        verify(blockingStub).createBillingAccount(any(BillingRequest.class));
    }
}
