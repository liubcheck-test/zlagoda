package helsinki.service.impl;

import helsinki.dao.CheckDao;
import helsinki.lib.Inject;
import helsinki.lib.Service;
import helsinki.model.Check;
import helsinki.service.CheckService;
import java.util.List;

@Service
public class CheckServiceImpl implements CheckService {

    @Inject
    private CheckDao checkDao;

    @Override
    public Check create(Check check) {
        return checkDao.save(check);
    }

    @Override
    public Check get(String s) {
        return checkDao.get(s).orElse(null);
    }

    @Override
    public List<Check> getAll() {
        return checkDao.getAll();
    }

    @Override
    public Check update(Check check) {
        return checkDao.update(check);
    }

    @Override
    public boolean delete(String s) {
        return checkDao.delete(s);
    }
}
