#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// Token types
typedef enum {
    TOKEN_INT,
    TOKEN_FLOAT,
    TOKEN_PLUS,
    TOKEN_MINUS,
    TOKEN_MULTIPLY,
    TOKEN_DIVIDE,
    TOKEN_LPAREN,
    TOKEN_RPAREN,
    TOKEN_IDENTIFIER,
    TOKEN_ASSIGN,
    TOKEN_SEMICOLON,
    TOKEN_EOF,
    TOKEN_ERROR
} TokenType;

// Token structure
typedef struct {
    TokenType type;
    char *value;
} Token;

// Symbol table
typedef struct {
    char *name;
    TokenType type;
} Symbol;

// Error handler
void error(const char *message) {
    fprintf(stderr, "Error: %s\n", message);
    exit(1);
}

// Lexer
Token get_next_token(char **text) {
    while (isspace(**text)) {
        (*text)++;
    }

    if (**text == '\0') {
        Token token = {TOKEN_EOF, NULL};
        return token;
    }

    if (isdigit(**text)) {
        char *value = *text;
        while (isdigit(**text) || **text == '.') {
            (*text)++;
        }
        int length = *text - value;
        char *num = (char *)malloc(length + 1);
        strncpy(num, value, length);
        num[length] = '\0';

        Token token;
        if (strchr(num, '.') == NULL) {
            token.type = TOKEN_INT;
        } else {
            token.type = TOKEN_FLOAT;
        }
        token.value = num;
        return token;
    }

    if (**text == '+') {
        (*text)++;
        Token token = {TOKEN_PLUS, "+"};
        return token;
    }

    if (**text == '-') {
        (*text)++;
        Token token = {TOKEN_MINUS, "-"};
        return token;
    }

    if (**text == '*') {
        (*text)++;
        Token token = {TOKEN_MULTIPLY, "*"};
        return token;
    }

    if (**text == '/') {
        (*text)++;
        Token token = {TOKEN_DIVIDE, "/"};
        return token;
    }

    if (**text == '(') {
        (*text)++;
        Token token = {TOKEN_LPAREN, "("};
        return token;
    }

    if (**text == ')') {
        (*text)++;
        Token token = {TOKEN_RPAREN, ")"};
        return token;
    }

    if (isalpha(**text)) {
        char *value = *text;
        while (isalnum(**text)) {
            (*text)++;
        }
        int length = *text - value;
        char *name = (char *)malloc(length + 1);
        strncpy(name, value, length);
        name[length] = '\0';

        Token token = {TOKEN_IDENTIFIER, name};
        return token;
    }

    if (**text == '=') {
        (*text)++;
        Token token = {TOKEN_ASSIGN, "="};
        return token;
    }

    if (**text == ';') {
        (*text)++;
        Token token = {TOKEN_SEMICOLON, ";"};
        return token;
    }

    Token token = {TOKEN_ERROR, NULL};
    return token;
}

// Parser
Token current_token;

void eat(TokenType type, char **text) {
    if (current_token.type == type) {
        current_token = get_next_token(text);
    } else {
        error("Unexpected token");
    }
}

void factor(char **text) {
    if (current_token.type == TOKEN_INT || current_token.type == TOKEN_FLOAT) {
        printf("PUSH %s\n", current_token.value);
        eat(current_token.type, text);
    } else if (current_token.type == TOKEN_IDENTIFIER) {
        printf("PUSH %s\n", current_token.value);
        eat(TOKEN_IDENTIFIER, text);
    } else if (current_token.type == TOKEN_LPAREN) {
        eat(TOKEN_LPAREN, text);
        expression(text);
        eat(TOKEN_RPAREN, text);
    }
}

void term(char **text) {
    factor(text);
    while (current_token.type == TOKEN_MULTIPLY || current_token.type == TOKEN_DIVIDE) {
        if (current_token.type == TOKEN_MULTIPLY) {
            eat(TOKEN_MULTIPLY, text);
            factor(text);
            printf("MUL\n");
        } else if (current_token.type == TOKEN_DIVIDE) {
            eat(TOKEN_DIVIDE, text);
            factor(text);
            printf("DIV\n");
        }
    }
}

void expression(char **text) {
    term(text);
    while (current_token.type == TOKEN_PLUS || current_token.type == TOKEN_MINUS) {
        if (current_token.type == TOKEN_PLUS) {
            eat(TOKEN_PLUS, text);
            term(text);
            printf("ADD\n");
        } else if (current_token.type == TOKEN_MINUS) {
            eat(TOKEN_MINUS, text);
            term(text);
            printf("SUB\n");
        }
    }
}

// Main function
int main() {
    char *text = "x = 10 + 20 * (30 - 40);";
    current_token = get_next_token(&text);
    while (current_token.type != TOKEN_EOF) {
        expression(&text);
        eat(TOKEN_ASSIGN, &text);
        printf("STORE %s\n", current_token.value);
        eat(TOKEN_IDENTIFIER, &text);
        eat(TOKEN_SEMICOLON, &text);
    }
    return 0;
}
