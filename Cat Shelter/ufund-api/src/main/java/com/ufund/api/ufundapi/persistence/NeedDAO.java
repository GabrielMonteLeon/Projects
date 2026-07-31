package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Need;

public interface NeedDAO {
    List<Need> getNeeds() throws IOException;
    Need getNeedByID(int id) throws IOException;
    List<Need> findNeeds(String searchText) throws IOException;
    Need createNeed(Need need) throws IOException;
    Need updateNeed(Need need) throws IOException;
    boolean deleteNeed(int id) throws IOException;

}
