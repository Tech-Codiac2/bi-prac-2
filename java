/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;
import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author admin
 */
class Block {
    private int index;
    private long timestamp;
    private String data;
    public String previousHash;
    public String hash;
    private int nonce;

    public Block(int index, long timestamp, String data, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
        this.nonce = 0;
    }

    private String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest((index + previousHash + timestamp + data + nonce).getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }
}

class Blockchain {
    private Block[] chain;
    private int difficulty;

    public Blockchain() {
        this.chain = new Block[] { createGenesisBlock() };
        this.difficulty = 4;
    }

    private Block createGenesisBlock() {
        return new Block(0, new Date().getTime(), "Genesis Block", "0");
    }

    public Block getLatestBlock() {
        return chain[chain.length - 1];
    }

    public void addBlock(Block newBlock) {
        newBlock.previousHash = getLatestBlock().hash;
        newBlock.mineBlock(difficulty);
        Block[] newChain = new Block[chain.length + 1];
        System.arraycopy(chain, 0, newChain, 0, chain.length);
        newChain[newChain.length - 1] = newBlock;
        chain = newChain;
    }
}
public class JavaApplication2 {
    public static void main(String[] args) {
        Blockchain ZeeCoin = new Blockchain();
        System.out.println("Mining block 1..");
        ZeeCoin.addBlock(new Block(1, new Date().getTime(), "{amount: 4}", ""));
        System.out.println("Mining block 2..");
        ZeeCoin.addBlock(new Block(2, new Date().getTime(), "{amount: 8}", ""));
    }
}
