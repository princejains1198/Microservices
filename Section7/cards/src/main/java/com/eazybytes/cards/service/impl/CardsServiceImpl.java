package com.eazybytes.cards.service.impl;

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardsDto;
import com.eazybytes.cards.entity.Cards;
import com.eazybytes.cards.exception.CardAlreadyExistsException;
import com.eazybytes.cards.exception.ResourceNotFoundException;
import com.eazybytes.cards.mapper.CardsMapper;
import com.eazybytes.cards.repository.CardsRepository;
import com.eazybytes.cards.service.CardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements CardsService {

    private CardsRepository cardsRepository;


    /**
     * Method to create a new card record in the database.
     *
     * @param mobileNumber@return true if the card record is successfully created, false otherwise.
     */
    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> cards = cardsRepository.findByMobileNumber((mobileNumber));
        if (cards.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobile number: " + mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
    }

    /**
     * Helper method to create a new Cards entity with default values.
     *
     * @param mobileNumber The mobile number associated with the card.
     * @return A new Cards entity.
     */
    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }

    /**
     * Method to fetch card details based on mobile number.
     *
     * @param mobileNumber The mobile number associated with the card.
     * @return CardsDto containing card details.
     */
    @Override
    public CardsDto fetchCardDetails(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
               () -> new ResourceNotFoundException("Card Details", "mobileNumber", mobileNumber)
        );
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    /**
     * Method to update card details.
     *
     * @param cardsDto Data Transfer Object containing updated card details.
     * @return true if the card record is successfully updated, false otherwise.
     */
    @Override
    public boolean updateCard(CardsDto cardsDto) {
        boolean isUpdatedCardetails = false;
        Cards existingCardDetails = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card Details", "mobileNumber", cardsDto.getMobileNumber())
        );
        if (existingCardDetails!=null) {
            Cards updatedCard = CardsMapper.mapToCards(cardsDto, existingCardDetails);
            cardsRepository.save(updatedCard);
            isUpdatedCardetails = true;
        }
        return isUpdatedCardetails;
    }

    /**
     * Method to delete a card record based on mobile number.
     *
     * @param mobileNumber The mobile number associated with the card to be deleted.
     * @return true if the card record is successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card Details", "mobileNumber", mobileNumber
        ));
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }


}
