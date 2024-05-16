package compiler;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Symbol> symbols = new HashMap<>();

    public void addSymbol(String name, String type) {
        symbols.put(name, new Symbol(name, type));
    }

    public Symbol getSymbol(String name) {
        return symbols.get(name);
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "symbols=" + symbols +
                '}';
    }
}
