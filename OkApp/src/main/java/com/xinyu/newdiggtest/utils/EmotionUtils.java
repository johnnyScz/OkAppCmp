
package com.xinyu.newdiggtest.utils;


import com.xinyu.newdiggtest.R;

import java.util.LinkedHashMap;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 * 表情加载类,可自己添加多种表情，分别建立不同的map存放和不同的标志符即可
 */
public class EmotionUtils {

    /**
     * key-表情文字;
     * value-表情图片资源
     */
    public static LinkedHashMap<String, Integer> EMPTY_GIF_MAP;
    public static LinkedHashMap<String, Integer> EMOTION_STATIC_MAP;


    static {

        EMPTY_GIF_MAP = new LinkedHashMap<>();
        EMPTY_GIF_MAP.put("[微笑]", R.drawable.emotion_weixiao_gif);

        EMOTION_STATIC_MAP = new LinkedHashMap<>();

        EMOTION_STATIC_MAP.put("[微笑]", R.drawable.emotion_weixiao);
        EMOTION_STATIC_MAP.put("[撇嘴]", R.drawable.emotion_biezui);
        EMOTION_STATIC_MAP.put("[色]", R.drawable.emotion_se);
        EMOTION_STATIC_MAP.put("[发呆]", R.drawable.emotion_fadai);
        EMOTION_STATIC_MAP.put("[得意]", R.drawable.emotion_deyi);
        EMOTION_STATIC_MAP.put("[流泪]", R.drawable.emotion_liulei);
        EMOTION_STATIC_MAP.put("[害羞]", R.drawable.emotion_haixiu);
        EMOTION_STATIC_MAP.put("[闭嘴]", R.drawable.emotion_bizui);
        EMOTION_STATIC_MAP.put("[睡]", R.drawable.emotion_shui);
        EMOTION_STATIC_MAP.put("[大哭]", R.drawable.emotion_daku);
        EMOTION_STATIC_MAP.put("[尴尬]", R.drawable.emotion_ganga);
        EMOTION_STATIC_MAP.put("[发怒]", R.drawable.emotion_fanu);
        EMOTION_STATIC_MAP.put("[调皮]", R.drawable.emotion_tiaopi);
        EMOTION_STATIC_MAP.put("[呲牙]", R.drawable.emotion_ciya);
        EMOTION_STATIC_MAP.put("[惊讶]", R.drawable.emotion_jingya);
        EMOTION_STATIC_MAP.put("[难过]", R.drawable.emotion_nanguo);
        EMOTION_STATIC_MAP.put("[酷]", R.drawable.emotion_ku);
        EMOTION_STATIC_MAP.put("[冷汗]", R.drawable.emotion_lenghan);
        EMOTION_STATIC_MAP.put("[抓狂]", R.drawable.emotion_zhuakuang);
        EMOTION_STATIC_MAP.put("[吐]", R.drawable.emotion_tu);
        EMOTION_STATIC_MAP.put("[偷笑]", R.drawable.emotion_touxiao);
        EMOTION_STATIC_MAP.put("[可爱]", R.drawable.emotion_keai);
        EMOTION_STATIC_MAP.put("[白眼]", R.drawable.emotion_baiyan);
        EMOTION_STATIC_MAP.put("[傲慢]", R.drawable.emotion_aoman);
        EMOTION_STATIC_MAP.put("[饥饿]", R.drawable.emotion_jie);
        EMOTION_STATIC_MAP.put("[困]", R.drawable.emotion_kun);
        EMOTION_STATIC_MAP.put("[惊恐]", R.drawable.emotion_jingkong);
        EMOTION_STATIC_MAP.put("[流汗]", R.drawable.emotion_liuhan);
        EMOTION_STATIC_MAP.put("[憨笑]", R.drawable.emotion_hanxiao);
        EMOTION_STATIC_MAP.put("[大兵]", R.drawable.emotion_dabing);
        EMOTION_STATIC_MAP.put("[奋斗]", R.drawable.emotion_fendou);
        EMOTION_STATIC_MAP.put("[咒骂]", R.drawable.emotion_zouma);
        EMOTION_STATIC_MAP.put("[疑问]", R.drawable.emotion_yiwen);
        EMOTION_STATIC_MAP.put("[嘘]", R.drawable.emotion_xu);
        EMOTION_STATIC_MAP.put("[晕]", R.drawable.emotion_yun);
        EMOTION_STATIC_MAP.put("[折磨]", R.drawable.emotion_fakuang);

        EMOTION_STATIC_MAP.put("[敲打]", R.drawable.emotion_qiaoda);
        EMOTION_STATIC_MAP.put("[再见]", R.drawable.emotion_zaijian);
        EMOTION_STATIC_MAP.put("[擦汗]", R.drawable.emotion_cahan);
        EMOTION_STATIC_MAP.put("[抠鼻]", R.drawable.emotion_koubi);
        EMOTION_STATIC_MAP.put("[鼓掌]", R.drawable.emotion_guzhang);
        EMOTION_STATIC_MAP.put("[糗大了]", R.drawable.emotion_qiudale);
        EMOTION_STATIC_MAP.put("[坏笑]", R.drawable.emotion_huaixiao);
        EMOTION_STATIC_MAP.put("[左哼哼]", R.drawable.emotion_zuohengheng);
        EMOTION_STATIC_MAP.put("[右哼哼]", R.drawable.emotion_youhengheng);
        EMOTION_STATIC_MAP.put("[哈欠]", R.drawable.emotion_haqian);
        EMOTION_STATIC_MAP.put("[鄙视]", R.drawable.emotion_bishi);
        EMOTION_STATIC_MAP.put("[委屈]", R.drawable.emotion_weiqu);
        EMOTION_STATIC_MAP.put("[快哭了]", R.drawable.emotion_kuaikule);
        EMOTION_STATIC_MAP.put("[阴险]", R.drawable.emotion_yingxian);
        EMOTION_STATIC_MAP.put("[亲亲]", R.drawable.emotion_qinqin);
        EMOTION_STATIC_MAP.put("[吓]", R.drawable.emotion_xia);
        EMOTION_STATIC_MAP.put("[可怜]", R.drawable.emotion_kelian);
        EMOTION_STATIC_MAP.put("[菜刀]", R.drawable.emotion_caidao);

        EMOTION_STATIC_MAP.put("[咖啡]", R.drawable.emotion_kafei);

        EMOTION_STATIC_MAP.put("[猪头]", R.drawable.emotion_zhutou);
        EMOTION_STATIC_MAP.put("[玫瑰]", R.drawable.emotion_meigui);
        EMOTION_STATIC_MAP.put("[凋谢]", R.drawable.emotion_diaoxie);
        EMOTION_STATIC_MAP.put("[示爱]", R.drawable.emotion_shiai);
        EMOTION_STATIC_MAP.put("[爱心]", R.drawable.emotion_aixin);
        EMOTION_STATIC_MAP.put("[心碎]", R.drawable.emotion_xinsui);

        EMOTION_STATIC_MAP.put("[炸弹]", R.drawable.emotion_zhadan);

        EMOTION_STATIC_MAP.put("[便便]", R.drawable.emotion_bianbian);

        EMOTION_STATIC_MAP.put("[拥抱]", R.drawable.emotion_baobao);
        EMOTION_STATIC_MAP.put("[强]", R.drawable.emotion_qiang);
        EMOTION_STATIC_MAP.put("[弱]", R.drawable.emotion_ruo);
        EMOTION_STATIC_MAP.put("[握手]", R.drawable.emotion_woshou);
        EMOTION_STATIC_MAP.put("[胜利]", R.drawable.emotion_shengli);
        EMOTION_STATIC_MAP.put("[抱拳]", R.drawable.emotion_baoquan);
        EMOTION_STATIC_MAP.put("[勾引]", R.drawable.emotion_gouying);

        EMOTION_STATIC_MAP.put("[差劲]", R.drawable.emotion_chajing);
        EMOTION_STATIC_MAP.put("[爱你]", R.drawable.emotion_aini);
        EMOTION_STATIC_MAP.put("[NO]", R.drawable.emotion_no);
        EMOTION_STATIC_MAP.put("[OK]", R.drawable.emotion_ok);
        EMOTION_STATIC_MAP.put("[爱情]", R.drawable.emotion_aiqing);
        EMOTION_STATIC_MAP.put("[飞吻]", R.drawable.emotion_feiwen);
        EMOTION_STATIC_MAP.put("[跳跳]", R.drawable.emotion_tiaotiao);
        EMOTION_STATIC_MAP.put("[发抖]", R.drawable.emotion_fadou);
        EMOTION_STATIC_MAP.put("[怄火]", R.drawable.emotion_ouhuo);
        EMOTION_STATIC_MAP.put("[转圈]", R.drawable.emotion_zhuanquan);
        EMOTION_STATIC_MAP.put("[磕头]", R.drawable.emotion_ketou);
        EMOTION_STATIC_MAP.put("[回头]", R.drawable.emotion_huitou);
        EMOTION_STATIC_MAP.put("[跳绳]", R.drawable.emotion_tiaosheng);
        EMOTION_STATIC_MAP.put("[挥手]", R.drawable.emotion_huishou);
        EMOTION_STATIC_MAP.put("[激动]", R.drawable.emotion_jidong);
        EMOTION_STATIC_MAP.put("[街舞]", R.drawable.emotion_jiewu);
        EMOTION_STATIC_MAP.put("[献吻]", R.drawable.emotion_xianwen);
        EMOTION_STATIC_MAP.put("[左太极]", R.drawable.emotion_zuotaiji);
        EMOTION_STATIC_MAP.put("[右太极]", R.drawable.emotion_youtaiji);

    }
}
