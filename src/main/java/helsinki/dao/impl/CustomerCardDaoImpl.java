package helsinki.dao.impl;

import helsinki.dao.CustomerCardDao;
import helsinki.exception.DataProcessingException;
import helsinki.lib.Dao;
import helsinki.model.CustomerCard;
import helsinki.model.composite.Address;
import helsinki.model.composite.FullName;
import helsinki.util.ConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Dao
public class CustomerCardDaoImpl implements CustomerCardDao {
    private final Random random = new Random();

    @Override
    public CustomerCard save(CustomerCard card) {
        String query = "INSERT INTO Customer_Card (card_number, cust_surname, cust_name, "
                + "cust_patronymic, phone_number, city, street, zip_code, percent) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement saveCardStatement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            long cardNumberLong = Math.abs(random.nextLong() % 10000000000000L);
            String cardNumber = String.format("%012d", cardNumberLong);
            saveCardStatement.setString(1, cardNumber);
            saveCardStatement.setString(2, card.getCustomerFullName().getSurname());
            saveCardStatement.setString(3, card.getCustomerFullName().getName());
            saveCardStatement.setString(4, card.getCustomerFullName().getPatronymic());
            saveCardStatement.setString(5, card.getPhoneNumber());
            saveCardStatement.setString(6, card.getCustomerAddress().getCity());
            saveCardStatement.setString(7, card.getCustomerAddress().getStreet());
            saveCardStatement.setString(8, card.getCustomerAddress().getZipCode());
            saveCardStatement.setInt(9, card.getPercent());
            saveCardStatement.executeUpdate();
            ResultSet resultSet = saveCardStatement.getGeneratedKeys();
            if (resultSet.next()) {
                card.setCardNumber(resultSet.getObject(1, String.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create " + card + ". ", e);
        }
        return card;
    }

    @Override
    public Optional<CustomerCard> get(String number) {
        String query = "SELECT * FROM Customer_Card WHERE card_number = ?";
        CustomerCard card = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getCardStatement = connection.prepareStatement(query)) {
            getCardStatement.setString(1, number);
            ResultSet resultSet = getCardStatement.executeQuery();
            if (resultSet.next()) {
                card = parseCustomerCardFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get customer card by number " + number, e);
        }
        return Optional.ofNullable(card);
    }

    @Override
    public List<CustomerCard> getAll() {
        String query = "SELECT * FROM Customer_Card";
        List<CustomerCard> cards = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllCardsStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllCardsStatement.executeQuery();
            while (resultSet.next()) {
                cards.add(parseCustomerCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of customer cards "
                    + "from customer cards database.", e);
        }
        return cards;
    }

    @Override
    public CustomerCard update(CustomerCard card) {
        String query = "UPDATE Customer_Card "
                + "SET cust_surname = ?, cust_name = ?, cust_patronymic = ?, "
                + "phone_number = ?, city = ?, street = ?, zip_code = ?, percent = ? "
                + "WHERE card_number = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateCardStatement = connection.prepareStatement(query)) {
            updateCardStatement.setString(1, card.getCustomerFullName().getSurname());
            updateCardStatement.setString(2, card.getCustomerFullName().getName());
            updateCardStatement.setString(3, card.getCustomerFullName().getPatronymic());
            updateCardStatement.setString(4, card.getPhoneNumber());
            updateCardStatement.setString(5, card.getCustomerAddress().getCity());
            updateCardStatement.setString(6, card.getCustomerAddress().getStreet());
            updateCardStatement.setString(7, card.getCustomerAddress().getZipCode());
            updateCardStatement.setInt(8, card.getPercent());
            updateCardStatement.setString(9, card.getCardNumber());
            updateCardStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update "
                    + card + " in customer cards database.", e);
        }
        return card;
    }

    @Override
    public boolean delete(String number) {
        String query = "DELETE FROM Customer_Card WHERE card_number = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteCardStatement = connection.prepareStatement(query)) {
            deleteCardStatement.setString(1, number);
            return deleteCardStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete customer card "
                    + "with number " + number, e);
        }
    }

    @Override
    public List<CustomerCard> getAllSortedBySurname() {
        String query = "SELECT * FROM Customer_Card ORDER BY cust_surname";
        List<CustomerCard> cards = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllCardsStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllCardsStatement.executeQuery();
            while (resultSet.next()) {
                cards.add(parseCustomerCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of customer cards "
                    + "from customer cards database.", e);
        }
        return cards;
    }

    @Override
    public List<CustomerCard> getAllByPercentSortedBySurname(Integer percent) {
        String query = "SELECT * FROM Customer_Card WHERE percent = ? ORDER BY cust_surname";
        List<CustomerCard> cards = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllCardsStatement = connection.prepareStatement(query)) {
            getAllCardsStatement.setInt(1, percent);
            ResultSet resultSet = getAllCardsStatement.executeQuery();
            while (resultSet.next()) {
                cards.add(parseCustomerCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of customer cards "
                    + "from customer cards database.", e);
        }
        return cards;
    }

    @Override
    public List<CustomerCard> getAllByLastName(String lastName) {
        String query = "SELECT * FROM Customer_Card "
                + "WHERE cust_surname LIKE ? "
                + "ORDER BY cust_surname, cust_name, cust_patronymic";
        List<CustomerCard> cards = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement getAllCardsStatement = connection.prepareStatement(query)) {
            getAllCardsStatement.setString(1, "%" + lastName + "%");
            ResultSet resultSet = getAllCardsStatement.executeQuery();
            while (resultSet.next()) {
                cards.add(parseCustomerCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of customer cards "
                    + "from customer cards database.", e);
        }
        return cards;
    }

    @Override
    public List<CustomerCard> requestCustomer(BigDecimal inputSum) {
        List<CustomerCard> customerCards = new ArrayList<>();
        String query = "SELECT DISTINCT C.card_number AS card_number, cust_surname, cust_name, cust_patronymic, phone_number, percent, sum_total "
                + "FROM `Check` INNER JOIN Customer_card AS C ON C.card_number = `Check`.card_number "
                + "WHERE NOT EXISTS ( "
                + "SELECT check_number "
                + "FROM `Check` INNER JOIN Customer_card ON Customer_card.card_number = `Check`.card_number "
                + "WHERE Customer_card.card_number = C.card_number "
                + "AND check_number NOT IN ( "
                + "SELECT check_number "
                + "FROM `Check` "
                + "WHERE sum_total >= ? "
                + "))";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement requestCustomerStatement = connection.prepareStatement(query)) {
            requestCustomerStatement.setBigDecimal(1, inputSum);
            ResultSet resultSet = requestCustomerStatement.executeQuery();
            while (resultSet.next()) {
                FullName fullName = new FullName(
                        resultSet.getString("cust_surname"),
                        resultSet.getString("cust_name"),
                        resultSet.getString("cust_patronymic")
                );

                CustomerCard customerCard = new CustomerCard();
                customerCard.setCardNumber(resultSet.getString("card_number"));
                customerCard.setCustomerFullName(fullName);
                customerCard.setPhoneNumber(resultSet.getString("phone_number"));
                customerCard.setPercent(resultSet.getInt("percent"));
                customerCards.add(customerCard);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't execute requestCustomer query. ", e);
        }
        return customerCards;
    }

    private CustomerCard parseCustomerCardFromResultSet(ResultSet resultSet) throws SQLException {
        CustomerCard customerCard = new CustomerCard();
        customerCard.setCardNumber(resultSet.getString("card_number"));
        customerCard.setCustomerFullName(new FullName(
                resultSet.getString("cust_surname"),
                resultSet.getString("cust_name"),
                resultSet.getString("cust_patronymic")
        ));
        customerCard.setPhoneNumber(resultSet.getString("phone_number"));
        customerCard.setCustomerAddress(new Address(
                resultSet.getString("city"),
                resultSet.getString("street"),
                resultSet.getString("zip_code")
        ));
        customerCard.setPercent(resultSet.getInt("percent"));
        return customerCard;
    }
}
