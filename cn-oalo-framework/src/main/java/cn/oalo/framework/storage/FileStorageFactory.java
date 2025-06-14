package cn.oalo.framework.storage;

import cn.oalo.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件存储工厂
 */
@Component
public class FileStorageFactory {

    /**
     * 存储类型
     */
    @Value("${file.storage.type:LOCAL}")
    private String storageType;

    /**
     * 存储策略映射
     */
    private final Map<StorageType, FileStorage> storageMap = new ConcurrentHashMap<>();

    /**
     * 注入所有存储策略
     */
    @Autowired
    public FileStorageFactory(List<FileStorage> storageList) {
        for (FileStorage storage : storageList) {
            storageMap.put(storage.getType(), storage);
        }
    }

    /**
     * 获取文件存储策略
     *
     * @return 文件存储策略
     */
    public FileStorage getFileStorage() {
        StorageType type;
        try {
            type = StorageType.valueOf(storageType.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = StorageType.LOCAL;
        }
        FileStorage storage = storageMap.get(type);
        if (storage == null) {
            throw new BusinessException("未找到对应的存储策略：" + type);
        }
        return storage;
    }

    /**
     * 获取指定类型的文件存储策略
     *
     * @param type 存储类型
     * @return 文件存储策略
     */
    public FileStorage getFileStorage(StorageType type) {
        FileStorage storage = storageMap.get(type);
        if (storage == null) {
            throw new BusinessException("未找到对应的存储策略：" + type);
        }
        return storage;
    }
} 