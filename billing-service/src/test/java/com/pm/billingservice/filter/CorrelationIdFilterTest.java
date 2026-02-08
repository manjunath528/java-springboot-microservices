package com.pm.billingservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CorrelationIdFilterTest {

    @Test
    void addsCorrelationIdToMdcAndClearsAfterRequest() throws Exception {
        CorrelationIdFilter filter = new CorrelationIdFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("X-Correlation-Id")).thenReturn("corr-123");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(MDC.get("correlationId")).isNull();
    }

    @Test
    void continuesWhenCorrelationIdMissing() throws Exception {
        CorrelationIdFilter filter = new CorrelationIdFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("X-Correlation-Id")).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(MDC.get("correlationId")).isNull();
    }
}
