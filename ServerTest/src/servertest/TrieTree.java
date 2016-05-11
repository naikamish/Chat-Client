/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

/**
 *
 * @author Amish Naik
 */
public class TrieTree {
    TrieNode root = new TrieNode('.');
    
    public void addWord(String word, int posNeg){
        TrieNode currNode = root;
        String currWord = word.toUpperCase();
        for(int i=0; i<currWord.length(); i++){
            int currChar = currWord.charAt(i)-65;
            if(currChar<0||currChar>25){
                continue;
            }
            if(currNode.next[currChar]==null){
                currNode.next[currChar] = new TrieNode(currWord.charAt(i));
            }
            currNode = currNode.next[currChar];
        }
        currNode.posNeg = posNeg;
    }
    
    public int findWord(String word){
        TrieNode currNode = root;
        String currWord = word.toUpperCase();
        for(int i=0; i<currWord.length(); i++){
            int currChar = currWord.charAt(i)-65;
            if(currNode.next[currChar]==null){
                return 0;
            }
            currNode = currNode.next[currChar];
        }
        return currNode.posNeg;
    }
    
    public class TrieNode{
        TrieNode[] next = new TrieNode[26];
        int posNeg = 0;
        char value;
        
        public TrieNode(char c){
            this.value = c;
        }
    }
}
