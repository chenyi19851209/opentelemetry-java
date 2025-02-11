/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.sdk.autoconfigure;

import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.metrics.SdkMeterProviderConfigurer;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.common.InstrumentType;
import io.opentelemetry.sdk.metrics.processor.LabelsProcessorFactory;
import io.opentelemetry.sdk.metrics.view.InstrumentSelector;
import io.opentelemetry.sdk.metrics.view.View;

public class TestMeterProviderConfigurer implements SdkMeterProviderConfigurer {

  @Override
  public void configure(SdkMeterProviderBuilder meterProviderBuilder, ConfigProperties config) {
    LabelsProcessorFactory labelsProcessorFactory =
        (resource, instrumentationLibraryInfo, descriptor) ->
            (ctx, labels) ->
                labels.toBuilder()
                    .put("configured", config.getBoolean("otel.test.configured"))
                    .build();

    for (InstrumentType instrumentType : InstrumentType.values()) {
      meterProviderBuilder.registerView(
          InstrumentSelector.builder().setInstrumentType(instrumentType).build(),
          View.builder().setLabelsProcessorFactory(labelsProcessorFactory).build());
    }
  }
}
