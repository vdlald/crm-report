package com.vladislav.crm.report.utils;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class LocalizedWeek {

    private final Locale locale;
    private final ZoneId zoneId;

    private DayOfWeek firstDayOfWeek;
    private DayOfWeek lastDayOfWeek;

    public LocalizedWeek() {
        this.locale = Locale.getDefault();
        this.zoneId = ZoneId.systemDefault();
        postConstruct(locale);
    }

    public LocalizedWeek(final Locale locale, ZoneId zoneId) {
        this.locale = locale;
        this.zoneId = zoneId;
        postConstruct(locale);
    }

    private void postConstruct(Locale locale) {
        this.firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
        this.lastDayOfWeek = DayOfWeek.of(((this.firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
    }

    public LocalDate getFirstWeekDate() {
        return LocalDate.now(zoneId).with(TemporalAdjusters.previousOrSame(this.firstDayOfWeek));
    }

    public LocalDate getLastWeekDate() {
        return LocalDate.now(zoneId).with(TemporalAdjusters.nextOrSame(this.lastDayOfWeek));
    }

    public LocalDateTime getFirstWeekDateTime() {
        return LocalDateTime.of(getFirstWeekDate(), LocalTime.MIN);
    }

    public LocalDateTime getLastWeekDateTime() {
        return LocalDateTime.of(getLastWeekDate(), LocalTime.MAX);
    }

    @Override
    public String toString() {
        return String.format("The %s week starts on %s and ends on %s",
                this.locale.getDisplayName(),
                this.firstDayOfWeek,
                this.lastDayOfWeek);
    }
}
