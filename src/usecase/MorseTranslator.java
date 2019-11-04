package usecase;
import material.Position;
import material.tree.binarytree.BinaryTree;
import material.tree.binarytree.LinkedBinaryTree;

public class MorseTranslator {
    //TODO: Practica 3 Ejercicio 3
    BinaryTree<Character> morseTree = new LinkedBinaryTree<>();



    /**
     * Generates a new MorseTranslator instance given two arrays:
     * one with the character set and another with their respective
     * morse code.
     *
     * @param charset
     * @param codes
     */
    public MorseTranslator (char[] charset, String[] codes) {
        int charsetCounter = 0;
        //Initializing morse tree.
        this.morseTree.addRoot('#');
        //Position that adds elements to the tree.
        Position<Character> pAllocator = this.morseTree.root();

        for (String s: codes) {
            for (int i = 0; i < s.length(); i++) {
                //move left
                if (s.charAt(i) == '.') {
                    if (i == s.length() - 1)
                        this.morseTree.insertLeft(pAllocator, charset[charsetCounter]);
                    else
                        pAllocator = this.morseTree.left(pAllocator);
                }
                //move right
                else if (s.charAt(i) == '-') {
                    if (i == s.length() - 1)
                        this.morseTree.insertRight(pAllocator, charset[charsetCounter]);
                    else
                        pAllocator = this.morseTree.right(pAllocator);
                }
            }
            charsetCounter++;
            pAllocator = this.morseTree.root();
        }
    }

    /**
     * Decodes a String with a message in morse code and returns
     * another String in plaintext. The input String may contain
     * the characters: ' ', '-' '.'.
     *
     * @param morseMessage
     * @return a plain text translation of the morse code
     */
    public String decode(String morseMessage) {
        Position<Character> pDecoder = this.morseTree.root();
        StringBuilder translation = new StringBuilder();
        int spaceDetector = 0;

        for (int i = 0; i < morseMessage.length(); i++) {
            //move left
            if (morseMessage.charAt(i) == '.') {
                pDecoder = this.morseTree.left(pDecoder);
                if (i == morseMessage.length()-1)
                    translation.append(pDecoder.getElement());
                spaceDetector = 0;
            }
            //move right
            else if (morseMessage.charAt(i) == '-') {
                pDecoder = this.morseTree.right(pDecoder);
                if (i == morseMessage.length()-1)
                    translation.append(pDecoder.getElement());
                spaceDetector = 0;
            }
            //Go back to the top of the tree for decoding next morse message
            else if (morseMessage.charAt(i) == ' ') {
                if (spaceDetector > 0)
                    translation.append(' ');
                else {
                    translation.append(pDecoder.getElement());
                    pDecoder = this.morseTree.root();
                }
                spaceDetector++;
            }
        }

        return translation.toString();
    }

    /**
     * Receives a String with a message in plaintext. This message
     * may contain any character in the charset.
     *
     * @param plainText
     * @return a morse code message
     */
    public String encode(String plainText) {
        Position<Character> pEncoder = this.morseTree.root();
        StringBuilder translation = new StringBuilder();
        StringBuilder encoder = new StringBuilder();
        int spaceDetector = 0;

        for (int i = 0; i < plainText.length(); i++) {
            //move left
            if (plainText.charAt(i) == (pEncoder.getElement())) {
                translation.append(encoder);
            }
            //move right
            else if (plainText.charAt(i) == '-') {
                pEncoder = this.morseTree.right(pEncoder);
                if (i == plainText.length()-1)
                    encoder.append("- ");
                spaceDetector = 0;
            }
            //Go back to the top of the tree for decoding next morse message
            else if (plainText.charAt(i) == ' ') {
                if (spaceDetector > 0)
                    encoder.append(' ');
                else {
                    encoder.append(' ');
                    pEncoder = this.morseTree.root();
                }
                spaceDetector++;
            }
        }

        return translation.toString();
    }

    private String encodeLetter (Character c) {
        StringBuilder encodedLetter = new StringBuilder();
        Position<Character> pEncoder = this.morseTree.root();




        return encodedLetter.toString();
    }


}
