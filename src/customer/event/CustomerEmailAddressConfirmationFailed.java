package customer.event;

import customer.value.ID;

public final class CustomerEmailAddressConfirmationFailed implements Event {
    private final ID customerID;

    private CustomerEmailAddressConfirmationFailed(ID customerID) {
        this.customerID = customerID;
    }

    public static CustomerEmailAddressConfirmationFailed build(ID customerID) {
        return new CustomerEmailAddressConfirmationFailed(customerID);
    }

    public ID customerID() {
        return customerID;
    }
}