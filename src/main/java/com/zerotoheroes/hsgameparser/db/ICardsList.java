package com.zerotoheroes.hsgameparser.db;

import java.util.List;

interface ICardsList {

    List<Card> getCards();

    Card find(String cardId);

    Card fromDbfId(int dbfId);
}
