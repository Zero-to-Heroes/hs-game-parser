package com.zerotoheroes.hsgameparser.db;

import java.util.List;

interface ICardsList {

    List<DbCard> getDbCards();

    DbCard findDbCard(String cardId);

    DbCard dbCardFromDbfId(int dbfId);
}
