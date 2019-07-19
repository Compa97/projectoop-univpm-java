package com.univpm.projectoop.utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Interfaccia necessaria per utilizzare il metodo con cui filtrare
 * @param <E> Tipo di oggetti della lista
 * @param <T> Valori di confronto
 */
public interface Filter<E,T> {

    /**
     * Metodo da implementare per ritornare la lista filtrata
     * @param fieldName Campo su cui filtrare
     * @param operator Operatore di filtraggio
     * @param value Valore di confronto
     * @return Ritorna la lista generata dal metodo select della classe FilterUtils
     * @throws NoSuchMethodException Lanciato quando un metodo non viene trovato
     * @throws IllegalAccessException Lanciato quando un'applicazione non ha accesso alla
     * definizione di una classe, un campo, un metodo o un costruttore
     * @throws InvocationTargetException Lanciato se un metodo chiamato contiene a sua volta un'eccezione
     */
    Collection<E> filterField(String fieldName, String operator, T... value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}