package com.javaweb.service.impl;
import com.javaweb.model.mongo_entity.userIndicator;
import com.javaweb.repository.UserIndicatorRepository;
import com.javaweb.service.IUserIndicatorService;
import lombok.Getter;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Types;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserIndicatorService implements IUserIndicatorService {
    @Autowired
    private UserIndicatorRepository userIndicatorRepository;

    @Override
    public void addIndicator(userIndicator userIndicator) {
        validateGroovySyntax(userIndicator.getCode(), userIndicator.getName());
        userIndicatorRepository.save(userIndicator);
    }

    @Override
    public String getCode(String username, String name) {
        Optional<userIndicator> userIndicatorOptional = userIndicatorRepository.findByUsernameAndName(username, name);
        return userIndicatorOptional.map(userIndicator::getCode).orElse(null);
    }

    @Override
    public Optional<userIndicator> findByUsernameAndName(String username, String name) {
        return userIndicatorRepository.findByUsernameAndName(username, name);
    }

    private void validateGroovySyntax(String scriptCode, String variableName) {
        CompilerConfiguration config = new CompilerConfiguration();
        SecureASTCustomizer secure = getSecureASTCustomizer();
        config.addCompilationCustomizers(secure);
        try {
            SourceUnit source = SourceUnit.create("script", scriptCode);
            source.configure(config);
            source.parse();
            source.completePhase();
            source.convert();

            AssignmentVisitor visitor = new AssignmentVisitor(variableName, source);
            source.getAST().getClasses().forEach(classNode -> classNode.visitContents(visitor));

            if (!visitor.isAssignmentFound()) {
                throw new IllegalArgumentException("Mã phải chứa một phép gán cho biến: " + variableName);
            }
        } catch (MultipleCompilationErrorsException e) {
            e.getErrorCollector().getErrors().forEach(error -> {
                if (error instanceof SyntaxErrorMessage) {
                    SyntaxException syntaxException = ((SyntaxErrorMessage) error).getCause();
                    String message = String.format("Lỗi cú pháp tại dòng %d, cột %d: %s",
                            syntaxException.getLine(),
                            syntaxException.getStartColumn(),
                            syntaxException.getMessage());
                    throw new IllegalArgumentException(message);
                }
            });
        }
    }

    private static class AssignmentVisitor extends ClassCodeVisitorSupport {
        private final String variableName;
        @Getter
        private boolean assignmentFound = false;
        private final SourceUnit sourceUnit;

        public AssignmentVisitor(String variableName, SourceUnit sourceUnit) {
            this.variableName = variableName;
            this.sourceUnit = sourceUnit;
        }
        @Override
        public void visitExpressionStatement(ExpressionStatement statement) {
            Expression expression = statement.getExpression();
            if (expression instanceof BinaryExpression) {
                BinaryExpression binaryExpression = (BinaryExpression) expression;
                if (binaryExpression.getOperation().getType() == Types.ASSIGN) {
                    Expression leftExpression = binaryExpression.getLeftExpression();
                    if (leftExpression instanceof VariableExpression) {
                        VariableExpression varExpr = (VariableExpression) leftExpression;
                        if (varExpr.getName().equals(variableName)) {
                            assignmentFound = true;
                        }
                    }
                }
            }
            super.visitExpressionStatement(statement);
        }
        @Override
        protected SourceUnit getSourceUnit() {
            return sourceUnit;
        }
    }

    @NotNull
    private SecureASTCustomizer getSecureASTCustomizer() {
        SecureASTCustomizer secure = new SecureASTCustomizer();

        secure.setClosuresAllowed(false);
        secure.setMethodDefinitionAllowed(false);
        secure.setPackageAllowed(false);
        secure.setImportsWhitelist(Collections.emptyList());
        secure.setStaticImportsWhitelist(Collections.emptyList());
        secure.setStarImportsWhitelist(Collections.emptyList());

        secure.setTokensWhitelist(Arrays.asList(
                org.codehaus.groovy.syntax.Types.ASSIGN,
                org.codehaus.groovy.syntax.Types.PLUS,
                org.codehaus.groovy.syntax.Types.MINUS,
                org.codehaus.groovy.syntax.Types.MULTIPLY,
                org.codehaus.groovy.syntax.Types.DIVIDE,
                org.codehaus.groovy.syntax.Types.MOD,
                org.codehaus.groovy.syntax.Types.POWER
        ));

        return secure;
    }



}
