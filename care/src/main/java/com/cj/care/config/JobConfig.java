package com.cj.care.config;


import com.cj.care.job.BlessingElasticJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {

    @Bean
    public CoordinatorRegistryCenter registryCenter(@Value("${zookeeper.url}") String url, @Value("${zookeeper.groupName}") String groupName) {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(url, groupName);
        //设置节点超时时间
        zookeeperConfiguration.setSessionTimeoutMilliseconds(100);
        CoordinatorRegistryCenter registryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
        registryCenter.init();
        return registryCenter;
    }

    private LiteJobConfiguration createJobConfiguration(Class clazz, String cron, int shardingCount) {
        //定义作业核心配置newBuilder("任务名称", "cron表达式", "分片数量")
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(clazz.getSimpleName(), cron, shardingCount).build();
        //定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, BlessingElasticJob.class.getCanonicalName());
        //定义Lite作业根配置
        return LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
    }

    @Bean(initMethod = "init")
    public SpringJobScheduler blessingScheduler(BlessingElasticJob job, CoordinatorRegistryCenter registryCenter) {
        LiteJobConfiguration jobConfiguration = createJobConfiguration(job.getClass(), "0 0 8 * * ?", 1);
        return new SpringJobScheduler(job, registryCenter, jobConfiguration);
    }

}
