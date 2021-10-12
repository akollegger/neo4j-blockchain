package blockchain;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

/**
 * This is an example showing how you could expose Neo4j's full text indexes as
 * two procedures - one for updating indexes, and one for querying by label and
 * the lucene query language.
 */
public class BlockchainAPI {

    static String boxedValue = "Hello from inside the box!";

    static Ledger primaryLedger = new Ledger();

    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;

    /**
     * Put a value to be stored somewhere for later retrieval.
     *
     * @param value  plain string value to store
     * @return  the value that was stored
     */
    @Procedure(value = "blockchain.put", mode = Mode.WRITE)
    @Description("Puts a value.")
    public Stream<BoxedValueRecord> setValue(@Name("value") String value) {
        boxedValue = value;

        return Stream.of(new BoxedValueRecord(boxedValue));
    }

    /**
     * Get the previously stored value.
     *
     * @return  A RelationshipTypes instance with the relations (incoming and outgoing) for a given node.
     */
    @Procedure(value = "blockchain.get", mode = Mode.READ)
    @Description("Gets a value.")
    public Stream<BoxedValueRecord> getValue() {
        return Stream.of(new BoxedValueRecord(boxedValue));
    }

    /**
     * Records data into the blockchain.
     *
     * @param data  plain string data to record
     * @return  the block in which the data was recorded
     */
    @Procedure(value = "blockchain.record", mode = Mode.WRITE)
    @Description("Records data into a new block.")
    public Stream<BlockRecord> recordValue(@Name("data") String data) {
        var recordedBlock = primaryLedger.record(data);

        return Stream.of(new BlockRecord(recordedBlock));
    }


    /**
     * Lists all blocks within the blockchain.
     *
     * @return  all blocks as a list
     */
    @Procedure(value = "blockchain.list", mode = Mode.READ)
    @Description("Records data into a new block.")
    public Stream<BlockRecord> listBlocks() {
        return primaryLedger.list().stream().map(block -> new BlockRecord(block));
    }

    /**
     * Validates the blockchain.
     *
     * @return true if the blockchain is valid, false otherwise
     */
    @UserFunction(value = "blockchain.validate")
    @Description("Verifies all hashes within the blockchain.")
    public boolean validateBlockchain() {
        return primaryLedger.validate();
    }


    /**
     * Corrupts the blockchain, for demonstration purposes.
     */
    @Procedure(value = "blockchain.corrupt")
    @Description("Corrupts data in a block, invalidating the blockchain.")
    public void corruptTheBlockchain() {
        primaryLedger.corrupt();
    }


    /**
     * Resets the blockchain by creating a fresh ledger.
     *
     */
    @Procedure(value = "blockchain.reset")
    @Description("Resets the blockchain.")
    public void reset() {
        primaryLedger = new Ledger();
    }

    /**
     * This is the output record for our get/set procedures. 
     * 
     * All procedures that return results return them as a Stream of Records, 
     * where the records are defined like this one - customized to fit what the 
     * procedure is returning.
     * <p>
     * These classes can only have public non-final fields, and the fields must
     * be one of the following types:
     *
     * <ul>
     *     <li>{@link String}</li>
     *     <li>{@link Long} or {@code long}</li>
     *     <li>{@link Double} or {@code double}</li>
     *     <li>{@link Number}</li>
     *     <li>{@link Boolean} or {@code boolean}</li>
     *     <li>{@link Node}</li>
     *     <li>{@link org.neo4j.graphdb.Relationship}</li>
     *     <li>{@link org.neo4j.graphdb.Path}</li>
     *     <li>{@link Map} with key {@link String} and value {@link Object}</li>
     *     <li>{@link List} of elements of any valid field type, including {@link List}</li>
     *     <li>{@link Object}, meaning any of the valid field types</li>
     * </ul>
     */
    public static class BoxedValueRecord {

        public String value;

        public BoxedValueRecord(String value) {
            this.value = value;
        }
    }


    /**
     * This is the output record for Blocks. 
     * 
     * All procedures that return results return them as a Stream of Records, 
     * where the records are defined like this one - customized to fit what the 
     * procedure is returning.
     * <p>
     * These classes can only have public non-final fields, and the fields must
     * be one of the following types:
     *
     * <ul>
     *     <li>{@link String}</li>
     *     <li>{@link Long} or {@code long}</li>
     *     <li>{@link Double} or {@code double}</li>
     *     <li>{@link Number}</li>
     *     <li>{@link Boolean} or {@code boolean}</li>
     *     <li>{@link Node}</li>
     *     <li>{@link org.neo4j.graphdb.Relationship}</li>
     *     <li>{@link org.neo4j.graphdb.Path}</li>
     *     <li>{@link Map} with key {@link String} and value {@link Object}</li>
     *     <li>{@link List} of elements of any valid field type, including {@link List}</li>
     *     <li>{@link Object}, meaning any of the valid field types</li>
     * </ul>
     */
    public static class BlockRecord {
        public String data;
        public long timestamp;
        public String hash;

        public BlockRecord(Block block) {
            this.data = block.data;
            this.timestamp = block.timestamp;
            this.hash = block.hash;
        }
    }
}
