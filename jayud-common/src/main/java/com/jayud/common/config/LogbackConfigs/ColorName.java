package com.jayud.common.config.LogbackConfigs;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * @author william
 * @description
 * @Date: 2020-07-15 12:32
 */
public class ColorName extends ForegroundCompositeConverterBase<ILoggingEvent> {
    @Override
    protected String getForegroundColorCode(ILoggingEvent iLoggingEvent) {
        return ANSIConstants.YELLOW_FG;
    }
}
