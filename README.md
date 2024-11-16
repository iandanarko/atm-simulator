# Test

ATM simulator service

## Prerequisite
- install java (https://openjdk.org/)
- install maven (https://maven.apache.org/)

## Running

```bash
mvn clean install
java -jar ./target/test-1.jar
```

## Command
* `login [name]` - Logs in as this customer and creates the customer if not exist

* `deposit [amount]` - Deposits this amount to the logged in customer

* `withdraw [amount]` - Withdraws this amount from the logged in customer

* `transfer [target] [amount]` - Transfers this amount from the logged in customer to the target customer

* `logout` - Logs out of the current customer