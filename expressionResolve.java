public String expressionResolve(String expression)
{
    // resolve the parenthesis first (recursive call)
    while(expression.contains("(") && expression.charAt(expression.indexOf("(") + 1) != '-')
    {
        // extract the values of parenthesis
        String tempParenthesis = expression.substring(expression.indexOf("(") + 1, expression.indexOf(")"));
        String res = "";

        // check if it is an expression
        if(tempParenthesis.contains("^") || tempParenthesis.contains("/") || tempParenthesis.contains("*") || tempParenthesis.contains("+") || tempParenthesis.contains("-"))
        {
            // then calculate the result
            res = expressionResolve(tempParenthesis);
        }
        else
        {
            // else it is a single number so the result is equal to the number
            res = tempParenthesis;
        }

        // swap the result calculated before with the values of the parenthesis
        expression = expression.replace("(" + tempParenthesis + ")", res);
    }

    // array sorted by the operations priority
    char priority[] = {'^', '/', '*'};

    // vectors storing operands and operators
    Vector operands = new Vector();
    Vector operators = new Vector();
    double temp = 0;

    String original = expression;

    // swap the operators symbols with the relative mathematical symbol e delete the others
    expression = expression.replaceAll("÷", "/").replaceAll("×", "*").replaceAll("[()]", "").replaceAll("[\\^][-]", "^");

    // fill the operators vector by extracting them from the given expression
    for(int i=0; i<expression.length(); i++)
    {
        if(expression.charAt(i) == '^' || expression.charAt(i) == '/' || expression.charAt(i) == '*' || expression.charAt(i) == '+' || expression.charAt(i) == '-')
        {
            operators.add(expression.charAt(i));
        }
    }

    // fill the operands vector by extracting them from the given expression, eventually removing the parenthesis
    Collections.addAll(operands, expression.split("[\\^/*+-]"));

    // check if there are no operators
    if(operators.size() == 0)
    {
        // then return the original expression
        return expressionValue;
    }
    else
    {
        // else, if the first operands is negative
        if(original.charAt(0) == '-')
        {
            // remove the first element of the correspondent vector
            operands.remove(0);

            // make the value negative
            String negativeValue = "-" + operands.elementAt(0).toString();

            // edit the value into the vector
            operands.setElementAt(negativeValue, 0);

            // remove the operator
            operators.remove(0);
        }

        // calculate powers, divisions and multiplications
        for(int i=0; i<priority.length; i++)
        {
            while(operators.contains(priority[i]))
            {
                int index = operators.indexOf(priority[i]);
                double op1 = Double.parseDouble(operands.elementAt(index).toString().replaceAll(",", "."));
                double op2 = Double.parseDouble(operands.elementAt(index+1).toString().replaceAll(",", "."));
                
                switch(priority[i])
                {
                    case '^':
                        // negative exponent
                        if(original.contains("(-" + String.format("%.0f", op2) + ")"))
                        {
                            op2 *= -1;
                        }
                        
                        // if the exponent is even but the value is negative, then make it even 
                        if((int) op2 % 2 == 0 && operators.size() > 1 && (char) operators.elementAt(index - 1) == '-')
                        {
                            operators.setElementAt('+', index - 1);
                        }

                        temp = Math.pow(op1, op2);
                        break;

                    case '/':
                        if(op2 != 0)
                        {
                            temp = op1 / op2;
                        }
                        else
                        {
                            return "Impossibile";
                        }
                        break;

                    case '*':
                        temp = op1 * op2;
                        break;
                }

                operands.setElementAt(String.format("%.2f", temp).replaceAll(",", "."), index);

                operands.remove(index+1);
                operators.remove(index);
            }
        }

        // calculate sums and subtractions
        while(operands.size() > 1)
        {
            double op1 = Double.parseDouble(operands.elementAt(0).toString().replaceAll(",", "."));
            double op2 = Double.parseDouble(operands.elementAt(1).toString().replaceAll(",", "."));

            switch((char) operators.elementAt(0))
            {
                case '+':
                    temp = op1 + op2;
                    break;
                
                case '-':
                    temp = op1 - op2;
                    break;
            }
            
            operands.setElementAt(String.format("%.2f", temp).replaceAll(",", "."), 0);
            
            operands.remove(1);
            operators.remove(0);
        }

        String result = operands.elementAt(0).toString();

        // check if the result is an integer number
        if(result.split("\\.")[1].toString().equals("00"))
        {
            // then return only the integer part of the result
            return result.split("\\.")[0].toString();
        }
        else
        {
            // else return the complete result
            return result;
        }
    }
}