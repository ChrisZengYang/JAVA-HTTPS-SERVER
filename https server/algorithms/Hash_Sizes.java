package algorithms;

public enum Hash_Sizes {
    _256_MessageBlock(64),
    _384_MessageBlock(128),
    _512_MessageBlock(128),
    _256_HashSize(32),
    _384_HashSize(48),
    _512_HashSize(64);

    private final int value;

    Hash_Sizes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}