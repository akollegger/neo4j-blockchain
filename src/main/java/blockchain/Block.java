package blockchain;

import java.util.Date;

public class Block {

	// Every block contains
	// a hash, previous hash and
	// data of the transaction made
	public String hash;
	public String previousHash;
	public String data; 		// was private
	public long timestamp; 	// was private, pascalCased

	// Constructor for the block
	public Block(String data,
				String previousHash)
	{
		this.data = data;
		this.previousHash
			= previousHash;
		this.timestamp
			= new Date().getTime();
		this.hash
			= calculateHash();
	}

	// Function to calculate the hash
	public String calculateHash()
	{
		// Calling the "Crypt" class
		// to calculate the hash
		// by using the previous hash,
		// timestamp and the data
		String calculatedhash
			= Crypt.sha256(
				previousHash
				+ Long.toString(timestamp)
				+ data);

		return calculatedhash;
	}
}


