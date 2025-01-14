package ec.com.sofka.account.values.objects;


import ec.com.sofka.generics.interfaces.IValueObject;

public class AccountNumber implements IValueObject<String> {
    private  String value;

    private AccountNumber(final String value)  {
        this.value = validate(value);
    }

    public AccountNumber() {
    }

    public static AccountNumber of(final String value) {
        return new AccountNumber(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    private String validate(final String value){
        if(value == null){
            throw new IllegalArgumentException("The number can't be null");
        }

        if(value.isBlank()){
            throw new IllegalArgumentException("The number can't be empty");
        }

        if(value.length() != 9){
            throw new IllegalArgumentException("The number must have 9 characters");
        }

        if (!value.matches("[0-9]+")) {
            throw new IllegalArgumentException("The number must be numeric");
        }

        return value;
    }

}
