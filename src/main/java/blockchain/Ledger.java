package blockchain;

import java.util.ArrayList;
import java.util.List;

/**
 * A ledger is a managed sequence of blocks.
 */
public class Ledger {
  private ArrayList<Block> blockchain = new ArrayList<Block>();

  public Ledger() {
    this.blockchain.add(new Block("head", "0"));
  }

  public Block record(String data) {
    var recordedBlock = new Block(data, this.blockchain
      .get(this.blockchain.size() - 1).hash
    );
    this.blockchain.add(recordedBlock);
    return recordedBlock;
  } 

  public List<Block> list() {
    return blockchain;
  }

  public boolean validate() {
    Block currentBlock = blockchain.get(0);
    Block previousBlock;
  
    // Checking that the root block is valid
    // ABKNOTE: original example missed this condition
    if (!currentBlock
      .calculateHash()  
      .equals(
          currentBlock
              .hash)) {
      System.out.println(
          "Hashes are not equal");
      return false;
    }

    // Iterating through
    // all the blocks
    for (int i = 1;
         i < blockchain.size();
         i++) {
  
        // Storing the current block
        // and the previous block
        currentBlock = blockchain.get(i);
        previousBlock = blockchain.get(i - 1);
  
        // Checking if the current hash
        // is equal to the
        // calculated hash or not
        if (!currentBlock
                .calculateHash()  // ABKNOTE: revised to recalculate first
                 .equals(
                     currentBlock
                         .hash)) {
            System.out.println(
                "Hashes are not equal");
            return false;
        }
  
        // Checking of the previous hash
        // is equal to the calculated
        // previous hash or not
        if (!previousBlock
                 .calculateHash() // ABKNOTE: revised to recalculate first
                 .equals(
                     currentBlock
                         .previousHash)) {
            System.out.println(
                "Previous Hashes are not equal");
            return false;
        }
    }
  
    // If all the hashes are equal
    // to the calculated hashes,
    // then the blockchain is valid
    return true;
  }

  /**
   * Bad, bad developer! Do not corrupt the data!
   */
  public void corrupt() {
    this.blockchain.get((int)(Math.random() * blockchain.size())).data = "Corrupt!";
  }

}
