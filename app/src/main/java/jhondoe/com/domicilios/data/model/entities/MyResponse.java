package jhondoe.com.domicilios.data.model.entities;

import java.util.List;

public class MyResponse {
    public long multicast_id;
    public int succes;
    public int failure;
    public int canonical_ids;
    public List<Result> results;
}
