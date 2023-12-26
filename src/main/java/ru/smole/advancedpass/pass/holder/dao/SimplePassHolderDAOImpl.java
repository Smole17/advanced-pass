package ru.smole.advancedpass.pass.holder.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.smole.advancedpass.pass.holder.entity.SimplePassHolder;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class SimplePassHolderDAOImpl extends BaseDaoImpl<SimplePassHolder, Long> implements SimplePassHolderDAO {

    public SimplePassHolderDAOImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SimplePassHolder.class);
    }

    @Override
    @SneakyThrows
    public @NotNull Optional<SimplePassHolder> findByUniqueId(UUID uniqueId) {
        val holders = super.queryForEq("uuid", uniqueId);
        return holders.stream().findFirst();
    }
}
