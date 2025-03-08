package com.img;

/**
 * @author : IMG
 * @create : 2025/3/8
 */
@SuppressWarnings("unused")
public class CharacterInfo {
    private char character;
    private int frequency;

    public CharacterInfo(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "CharacterInfo{" +
                "character=" + character +
                ", frequency=" + frequency +
                '}';
    }
}
