import com.rose.utils.ServiceFactory;
import com.rose.workbench.domain.Clue;
import com.rose.workbench.domain.activity;
import com.rose.workbench.service.ActivityService;
import com.rose.workbench.service.ClueService;
import com.rose.workbench.service.impl.activityServiceImpl;
import com.rose.workbench.service.impl.clueServiceImpl;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MapTest {
    @Test
    public void Nun(){
       ActivityService service = (ActivityService) ServiceFactory.getService(new activityServiceImpl());
//        List<activity> map= service.sele();
//        System.out.println(map);
    }
}
