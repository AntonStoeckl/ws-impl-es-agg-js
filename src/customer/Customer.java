package customer;

import customer.command.ChangeCustomerEmailAddress;
import customer.command.ConfirmCustomerEmailAddress;
import customer.command.RegisterCustomer;
import customer.event.*;
import customer.value.Hash;

import java.util.List;

public final class Customer {
    private Hash confirmationHash;
    private boolean isEmailAddressConfirmed;

    private Customer() {
    }

    public static CustomerRegistered register(RegisterCustomer command) {
        return CustomerRegistered.build(
                command.id(),
                command.emailAddress(),
                command.confirmationHash(),
                command.name()
        );
    }

    public static Customer reconstitute(List<Event> events) {
        var customer = new Customer();

        for (Event event: events) {
            customer.apply(event);
        }

        return customer;
    }

    public List<Event> confirmEmailAddress(ConfirmCustomerEmailAddress command) {
        if (!confirmationHash.equals(command.confirmationHash)) {
            return List.of(CustomerEmailAddressConfirmationFailed.build(command.customerID()));
        }

        if (isEmailAddressConfirmed) {
            return List.of();
        }

        return List.of(CustomerEmailAddressConfirmed.build(command.customerID()));
    }

    public CustomerEmailAddressChanged ChangeEmailAddress(ChangeCustomerEmailAddress command) {
        return CustomerEmailAddressChanged.build(command.customerID(), command.emailAddress(), command.confirmationHash());
    }

    private void apply(Event event) {
        if (event.getClass() == CustomerRegistered.class) {
            confirmationHash = ((CustomerRegistered) event).confirmationHash();
        } else if (event.getClass() == CustomerEmailAddressConfirmed.class) {
            isEmailAddressConfirmed = true;
        }
    }
}
