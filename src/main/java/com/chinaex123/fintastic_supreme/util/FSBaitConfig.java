package com.chinaex123.fintastic_supreme.util;

import com.chinaex123.fintastic_supreme.config.FSConfig;

public class FSBaitConfig {

    /** 双倍鱼饵的触发概率 **/
    public static final double DOUBLE_CATCH_CHANCE = FSConfig.DOUBLE_CATCH_CHANCE.get();

    /** 多重掉落钓钩的额外掉落数量上限 **/
    public static final int MULTI_DROP_MAX_EXTRA = FSConfig.MULTI_DROP_MAX_EXTRA.get();

    /** 多重掉落钓钩的触发概率 **/
    public static final double MULTI_DROP_CHANCE = FSConfig.MULTI_DROP_CHANCE.get();

    /** 幸运鱼线幸运值提供的触发概率 **/
    public static final double LUCKY_LINE_LUCK_TO_CHANCE = FSConfig.LUCKY_LINE_LUCK_TO_CHANCE.get();

    /** 轻量鱼线延长逃跑时间的倍数 **/
    public static final double LIGHTWEIGHT_LINE_ESCAPE_MULTIPLIER = FSConfig.LIGHTWEIGHT_LINE_ESCAPE_MULTIPLIER.get();
}