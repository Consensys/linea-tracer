package net.consensys.linea.sequencer.txselection;

import com.google.common.base.MoreObjects;
import picocli.CommandLine;

/**
 * The Linea CLI options.
 */
public class LineaTransactionSelectionCliOptions {
    public static final int DEFAULT_MAX_TX_CALLDATA_SIZE = 60000;
    public static final int DEFAULT_MAX_BLOCK_CALLDATA_SIZE = 70000;

    private static final String MAX_TX_CALLDATA_SIZE = "--plugin-linea-max-tx-calldata-size";
    private static final String MAX_BLOCK_CALLDATA_SIZE = "--plugin-linea-max-block-calldata-size";

    @CommandLine.Option(
            names = {MAX_TX_CALLDATA_SIZE},
            hidden = true,
            paramLabel = "<INTEGER>",
            description =
                    "Maximum size for the calldata of a Transaction (default: "
                            + DEFAULT_MAX_TX_CALLDATA_SIZE
                            + ")")
    private int maxTxCallDataSize = DEFAULT_MAX_TX_CALLDATA_SIZE;

    @CommandLine.Option(
            names = {MAX_BLOCK_CALLDATA_SIZE},
            hidden = true,
            paramLabel = "<INTEGER>",
            description =
                    "Maximum size for the calldata of a Block (default: "
                            + DEFAULT_MAX_BLOCK_CALLDATA_SIZE
                            + ")")
    private int maxBlockCallDataSize = DEFAULT_MAX_BLOCK_CALLDATA_SIZE;

    private LineaTransactionSelectionCliOptions() {
    }

    /**
     * Create Linea cli options.
     *
     * @return the Linea cli options
     */
    public static LineaTransactionSelectionCliOptions create() {
        return new LineaTransactionSelectionCliOptions();
    }

    /**
     * Linea cli options from config.
     *
     * @param config the config
     * @return the Linea cli options
     */
    public static LineaTransactionSelectionCliOptions fromConfig(final LineaTransactionSelectionConfiguration config) {
        final LineaTransactionSelectionCliOptions options = create();
        options.maxTxCallDataSize = config.maxTxCallDataSize();
        options.maxBlockCallDataSize = config.maxBlockCallDataSize();
        return options;
    }

    /**
     * To domain object Linea factory configuration.
     *
     * @return the Linea factory configuration
     */
    public LineaTransactionSelectionConfiguration toDomainObject() {
        return new LineaTransactionSelectionConfiguration.Builder()
                .maxTxCallDataSize(maxTxCallDataSize)
                .maxBlockCallDataSize(maxBlockCallDataSize)
                .build();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add(MAX_TX_CALLDATA_SIZE, maxTxCallDataSize)
                .add(MAX_BLOCK_CALLDATA_SIZE, maxBlockCallDataSize)
                .toString();
    }
}
