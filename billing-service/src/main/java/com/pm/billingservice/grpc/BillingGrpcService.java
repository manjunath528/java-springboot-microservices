package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.repository.BillingAccountRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

  private static final Logger log = LoggerFactory.getLogger(
      BillingGrpcService.class);

    private final BillingAccountRepository billingAccountRepository;
    public BillingGrpcService(BillingAccountRepository billingAccountRepository) {
        this.billingAccountRepository = billingAccountRepository;
    }

  @Override
  public void createBillingAccount(
          BillingRequest billingRequest,
          StreamObserver<BillingResponse> responseObserver) {

      log.info("createBillingAccount request received for userId={}",
              billingRequest.getUserId());

      try {
          // Save in DB
          BillingAccount account = new BillingAccount();
          account.setUserId(billingRequest.getUserId());
          account.setName(billingRequest.getName());
          account.setEmail(billingRequest.getEmail());
          account.setStatus("ACTIVE");

          BillingAccount saved = billingAccountRepository.save(account);

          BillingResponse response = BillingResponse.newBuilder()
                  .setAccountId(saved.getAccountId())
                  .setStatus(saved.getStatus())
                  .build();

          log.info("Billing account created for userId={} accountId={}",
                  saved.getUserId(), saved.getAccountId());

          responseObserver.onNext(response);
          responseObserver.onCompleted();

      } catch (Exception e) {
          log.error("Failed to create billing account for userId={}",
                  billingRequest.getUserId(), e);
          responseObserver.onError(
                  Status.INTERNAL
                          .withDescription("Error creating billing account")
                          .withCause(e)
                          .asRuntimeException()
          );
      }
  }

}
