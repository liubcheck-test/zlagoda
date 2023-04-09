package helsinki.service.impl;

import helsinki.dao.CheckDao;
import helsinki.lib.Inject;
import helsinki.lib.Service;
import helsinki.model.Check;
import helsinki.service.CheckService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Object> getAllInfoByNumber(String number) {
        return checkDao.getAllInfoByNumber(number);
    }

    @Override
    public List<Check> getAllByToday(String employeeId) {
        return checkDao.getAllByToday(employeeId);
    }

    @Override
    public List<Check> getAllChecksByPeriod(LocalDateTime from, LocalDateTime to) {
        return checkDao.getAllChecksByPeriod(from, to);
    }

    @Override
    public List<Check> getAllChecksByCashierAndPeriod(String employeeId, LocalDateTime from, LocalDateTime to) {
        return checkDao.getAllChecksByCashierAndPeriod(employeeId, from, to);
    }

    @Override
    public BigDecimal getTotalSumByCashierAndPeriod(String employeeId, LocalDateTime from, LocalDateTime to) {
        return checkDao.getTotalSumByCashierAndPeriod(employeeId, from, to);
    }

    @Override
    public BigDecimal getTotalSumByPeriod(LocalDateTime from, LocalDateTime to) {
        return checkDao.getTotalSumByPeriod(from, to);
    }

    @Override
    public Integer getTotalAmountByProductAndPeriod(String productId, LocalDateTime from, LocalDateTime to) {
        return checkDao.getTotalAmountByProductAndPeriod(productId, from, to);
    }
}
