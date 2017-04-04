package org.mule.common.query;

import java.util.HashSet;
import java.util.Set;

public enum DsqlKeyword 
{

        ASC,
        DESC,
        SELECT,
        FROM,
        WHERE,
        ORDER,
        BY,
        LIMIT,
        OFFSET,
        AND,
        OR,
        NOT,
        LIKE;    
    
    private static final Set<String> keywordsSet = initializeSet();
    
    private static Set<String> initializeSet() 
    {
        HashSet<String> keywordsSet = new HashSet<String>();
        for(DsqlKeyword keyword : DsqlKeyword.values()) 
        {
            keywordsSet.add(keyword.toString());
        }
        return keywordsSet;
    }

    public static boolean isKeywordIgnoreCase(String value) 
    {
        return keywordsSet.contains(value.toUpperCase());
    }
}
