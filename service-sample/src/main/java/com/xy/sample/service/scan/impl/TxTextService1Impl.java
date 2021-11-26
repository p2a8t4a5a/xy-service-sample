package com.xy.sample.service.scan.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.common.utils.FlApplicationContextCatch;
import com.xy.sample.entity.scan.SampleScan;
import com.xy.sample.mapper.scan.SampleScanMapper;
import com.xy.sample.service.scan.TxTextService1;
import com.xy.sample.service.scan.TxTextService2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TxTextService1Impl extends ServiceImpl<SampleScanMapper, SampleScan> implements TxTextService1 {

    @Autowired
    private TxTextService2 txTextService2;

    @Autowired
    private FlApplicationContextCatch applicationContextCatch;

    @Transactional
    @Override
    public void txOne(SampleScan scan) {
        log.info("txOne");
        scan.setId(null);
        scan.setName("txOne_" + System.currentTimeMillis());
        this.save(scan);

        /*
        txOne上@Transactional前置处理：开启“事务”
        txOne执行... 如果发生异常，txOne上@Transactional后置处理：执行rollback
        调用txTwo
            txTwo上@Transactional前置处理：Participating in existing transaction
            txTwo代码执行，没有抛出异常
            txTwo上@Transactional后置处理：do nothing
        txOne执行... 如果发生异常,txOne上@Transactional后置处理：执行rollback
        txOne上@Transactional后置处理: 执行commit
         */
        //txTextService2.txTwo(scan);

        /*
        txOne上@Transactional前置处理：开启“事务”
        txOne执行..., 如果发生异常，txOne上@Transactional后置处理：执行rollback
        调用txTwo2
            txTwo2上@Transactional前置处理：Participating in existing transaction
            txTwo2代码执行，抛出"异常1"
            txTwo2上@Transactional后置处理：Participating transaction failed - marking existing transaction as rollback-only,并抛出"异常1"
        如果txOne不捕获"异常1", 异常向上抛，那么txOne上@Transactional后置处理：执行rollback并且抛出"异常1"
        如果txOne捕获"异常1", txOne上@Transactional后置处理：执行commit，由于”事务“有rollback-only标记，会执行rollback并且抛出"异常2"
        */
        //txTextService2.txTwo2(scan);


        /*
        txOne上@Transactional前置处理：开启“事务”
        txOne执行..., 如果发生异常，txOne上@Transactional后置处理：执行rollback
        调用txTwo3
            txTwo3上@Transactional前置处理：Suspending current transaction, creating new transaction
            txTwo3代码执行，没有抛出异常
            txTwo3上@Transactional后置处理：执行commit
        txOne执行... 如果发生异常,txOne上@Transactional后置处理：执行rollback
        txOne上@Transactional后置处理: Resuming suspended transaction after completion of inner transaction,执行commit
        */
        //txTextService2.txTwo3(scan);


        /*
        txOne上@Transactional前置处理：开启“事务”
        txOne执行..., 如果发生异常，txOne上@Transactional后置处理：执行rollback
        调用txTwo4
            txTwo4上@Transactional前置处理：Suspending current transaction, creating new transaction
            txTwo4代码执行，抛出"异常1"
            txTwo4上@Transactional后置处理：执行rollback
        如果txOne不捕获"异常1", 异常向上抛，那么txOne上@Transactional后置处理：执行rollback并且抛出"异常1"
        如果txOne捕获"异常1", txOne上@Transactional后置处理：Resuming suspended transaction after completion of inner transaction，执行commit
        */
        try {
            //txTextService2.txTwo4(scan);
        } catch(Exception e) {
            //
        }


        /*
        txOne上@Transactional前置处理：开启“事务”
        txOne执行..., 如果发生异常，txOne上@Transactional后置处理：执行rollback
        调用txTwo5
            txTwo5上@Transactional前置处理：Creating nested transaction
            txTwo5代码执行，没有抛出异常
            txTwo5上@Transactional后置处理：Releasing transaction savepoint(nested事务commit状态，未提交)
        txOne执行... 如果发生异常,txOne上@Transactional后置处理：执行rollback，同时rollback nested
        txOne上@Transactional后置处理: 执行commit,同时commit nested
        */
        //txTextService2.txTwo5(scan);
        //int i = 1/0;

        /*
        txOne上@Transactional前置处理：开启“事务”
        txOne执行..., 如果发生异常，txOne上@Transactional后置处理：执行rollback
        调用txTwo6
            txTwo6上@Transactional前置处理：Creating nested transaction
            txTwo6代码执行，抛出"异常1"
            txTwo6上@Transactional后置处理：Rolling back transaction to savepoint(nested事务rollback状态)
        如果txOne不捕获"异常1", 异常向上抛，那么txOne上@Transactional后置处理：执行rollback同时rollback nested并且抛出"异常1"
        如果txOne捕获"异常1", txOne上@Transactional后置处理：执行commit,同时rollback nested
        */
        try {
            //txTextService2.txTwo6(scan);
        } catch (Exception e) {
            //
        }


    }

    @Transactional
    @Override
    public void txOne2(SampleScan scan) {
        log.info("txOne2");
        scan.setId(null);
        scan.setName("txOne2_" + System.currentTimeMillis());
        this.save(scan);


        System.out.println("this是否是AopProxy: " + AopUtils.isAopProxy(this));
        System.out.println("this是否是JdkProxy: " + AopUtils.isJdkDynamicProxy(this));
        System.out.println("this是否是CglibProxy: " + AopUtils.isCglibProxy(this));

        //this.txOne3(scan); //1、此this是被代理对象,txOne3上面的Transactional注解不会触发

        TxTextService1Impl thisProxy = applicationContextCatch.getBean(TxTextService1Impl.class);
        System.out.println(thisProxy.equals(this));

        thisProxy.txOne3(scan);   //2、使用代理对象调用txOne3方法,Suspending current transaction, creating new transaction with name "txOne3", "txOne3" commit, Resuming suspended transaction after completion of inner transaction
        //thisProxy.txOne4(scan);   //3、使用代理对象调用txOne4方法,Participating in existing transaction

        /*
         * this,super说明
         * super: 子类继承父类，子类构造时先构造父类，子类中使用super指向父类对象
         *        子类方法中通过super调用父类方法，此时父类方法中的this仍然是子类对象
         * this: 当前对象的引用
         *     1)、 Class A{ private String name; public void name(){this.name}  }
         *            A a = new A();
         *            a.name();  //name方法中的this就是a这个对象
         *     2)、Class S extends P; Class P{ private String name; public void name(){this.name}  }
         *            S s = new S();
         *            s.name();  //name方法中的this就是s这个对象
         *     3)、Method.invoke(target, args)
         *         反射执行方法时的this是target
         *
         */

        throw new RuntimeException("some error");
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    @Override
    public void txOne3(SampleScan scan) {
        log.info("txOne3");
        scan.setId(null);
        scan.setName("txOne3_" + System.currentTimeMillis());
        this.save(scan);
        //throw new RuntimeException("some error");
    }

    @Transactional
    @Override
    public void txOne4(SampleScan scan) {
        log.info("txOne4");
        scan.setId(null);
        scan.setName("txOne4_" + System.currentTimeMillis());
        this.save(scan);
        //throw new RuntimeException("some error");
    }

}
