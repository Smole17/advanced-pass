package ru.smole.advancedpass.pass.holder.dao;

import com.j256.ormlite.dao.Dao;
import org.jetbrains.annotations.NotNull;
import ru.smole.advancedpass.pass.holder.entity.SimplePassHolder;

import java.util.Optional;
import java.util.UUID;

public interface SimplePassHolderDAO extends Dao<SimplePassHolder, Long> {

    @NotNull Optional<SimplePassHolder> findByUniqueId(UUID uniqueId);
}
