package compiler;

public class Symbol {
    String name;
    String type;

    public Symbol(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Symbol{name='%s', type='%s'}", name, type);
    }
}
