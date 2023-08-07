package net.consensys.linea.zktracer.opcode.mxp;

import net.consensys.linea.zktracer.opcode.Gas;

enum BillingRate {
    ByWord, ByByte,
}

public record Billing (Gas perUnit, BillingRate billingRate) {
    public  Billing() {
        this(null, null);
    }

    public static Billing byWord(Gas perUnit) {
        return new Billing(perUnit, BillingRate.ByWord);
    }

    public static Billing byByte (Gas perUnit) {
        return new Billing(perUnit, BillingRate.ByByte);
    }
}