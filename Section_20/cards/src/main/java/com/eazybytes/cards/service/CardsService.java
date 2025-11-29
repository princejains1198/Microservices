package com.eazybytes.cards.service;

import com.eazybytes.cards.dto.CardsDto;

public interface CardsService {

    /**
     * Method to create a new card record.
     *
     * @param mobileNumber The mobile number associated with the new card.
     */
    void createCard(String mobileNumber);

    /**
     * Method to fetch card details based on mobile number.
     *
     * @param mobileNumber The mobile number associated with the card.
     * @return CardsDto containing card details.
     */
    CardsDto fetchCardDetails(String mobileNumber);

    /**
     * Method to update card details.
     *
     * @param cardsDto Data Transfer Object containing updated card details.
     * @return true if the card record is successfully updated, false otherwise.
     */
    boolean updateCard(CardsDto cardsDto);

    /**
     * Method to delete a card record based on mobile number.
     *
     * @param mobileNumber The mobile number associated with the card to be deleted.
     * @return true if the card record is successfully deleted, false otherwise.
     */
    boolean deleteCard(String mobileNumber);
}
