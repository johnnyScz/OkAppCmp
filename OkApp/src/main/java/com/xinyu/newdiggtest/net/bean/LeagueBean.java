package com.xinyu.newdiggtest.net.bean;

import java.util.List;

public class LeagueBean {

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {

        private String key;
        private TabsBean tabs;
        private ViewsBean views;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public TabsBean getTabs() {
            return tabs;
        }

        public void setTabs(TabsBean tabs) {
            this.tabs = tabs;
        }

        public ViewsBean getViews() {
            return views;
        }

        public void setViews(ViewsBean views) {
            this.views = views;
        }

        public static class TabsBean {
            /**
             * saicheng1 : 第37轮赛程
             * saicheng2 : 第38轮赛程
             * saicheng3 : null
             * jifenbang : 积分榜
             * sheshoubang : 射手榜
             */

            private String saicheng1;
            private String saicheng2;
            private Object saicheng3;
            private String jifenbang;
            private String sheshoubang;

            public String getSaicheng1() {
                return saicheng1;
            }

            public void setSaicheng1(String saicheng1) {
                this.saicheng1 = saicheng1;
            }

            public String getSaicheng2() {
                return saicheng2;
            }

            public void setSaicheng2(String saicheng2) {
                this.saicheng2 = saicheng2;
            }

            public Object getSaicheng3() {
                return saicheng3;
            }

            public void setSaicheng3(Object saicheng3) {
                this.saicheng3 = saicheng3;
            }

            public String getJifenbang() {
                return jifenbang;
            }

            public void setJifenbang(String jifenbang) {
                this.jifenbang = jifenbang;
            }

            public String getSheshoubang() {
                return sheshoubang;
            }

            public void setSheshoubang(String sheshoubang) {
                this.sheshoubang = sheshoubang;
            }
        }

        public static class ViewsBean {
            private Object saicheng3;
            private List<Saicheng1Bean> saicheng1;
            private List<Saicheng2Bean> saicheng2;
            private List<JifenbangBean> jifenbang;
            private List<SheshoubangBean> sheshoubang;

            public Object getSaicheng3() {
                return saicheng3;
            }

            public void setSaicheng3(Object saicheng3) {
                this.saicheng3 = saicheng3;
            }

            public List<Saicheng1Bean> getSaicheng1() {
                return saicheng1;
            }

            public void setSaicheng1(List<Saicheng1Bean> saicheng1) {
                this.saicheng1 = saicheng1;
            }

            public List<Saicheng2Bean> getSaicheng2() {
                return saicheng2;
            }

            public void setSaicheng2(List<Saicheng2Bean> saicheng2) {
                this.saicheng2 = saicheng2;
            }

            public List<JifenbangBean> getJifenbang() {
                return jifenbang;
            }

            public void setJifenbang(List<JifenbangBean> jifenbang) {
                this.jifenbang = jifenbang;
            }

            public List<SheshoubangBean> getSheshoubang() {
                return sheshoubang;
            }

            public void setSheshoubang(List<SheshoubangBean> sheshoubang) {
                this.sheshoubang = sheshoubang;
            }

            public static class Saicheng1Bean {

                private String c1;
                private String c2;
                private String c3;
                private String c4T1;
                private String c4T1URL;
                private String c4R;
                private String c4T2;
                private String c4T2URL;
                private String c51;
                private String c51Link;
                private String c52;
                private String c52Link;
                private String liveid;

                public String getC1() {
                    return c1;
                }

                public void setC1(String c1) {
                    this.c1 = c1;
                }

                public String getC2() {
                    return c2;
                }

                public void setC2(String c2) {
                    this.c2 = c2;
                }

                public String getC3() {
                    return c3;
                }

                public void setC3(String c3) {
                    this.c3 = c3;
                }

                public String getC4T1() {
                    return c4T1;
                }

                public void setC4T1(String c4T1) {
                    this.c4T1 = c4T1;
                }

                public String getC4T1URL() {
                    return c4T1URL;
                }

                public void setC4T1URL(String c4T1URL) {
                    this.c4T1URL = c4T1URL;
                }

                public String getC4R() {
                    return c4R;
                }

                public void setC4R(String c4R) {
                    this.c4R = c4R;
                }

                public String getC4T2() {
                    return c4T2;
                }

                public void setC4T2(String c4T2) {
                    this.c4T2 = c4T2;
                }

                public String getC4T2URL() {
                    return c4T2URL;
                }

                public void setC4T2URL(String c4T2URL) {
                    this.c4T2URL = c4T2URL;
                }

                public String getC51() {
                    return c51;
                }

                public void setC51(String c51) {
                    this.c51 = c51;
                }

                public String getC51Link() {
                    return c51Link;
                }

                public void setC51Link(String c51Link) {
                    this.c51Link = c51Link;
                }

                public String getC52() {
                    return c52;
                }

                public void setC52(String c52) {
                    this.c52 = c52;
                }

                public String getC52Link() {
                    return c52Link;
                }

                public void setC52Link(String c52Link) {
                    this.c52Link = c52Link;
                }

                public String getLiveid() {
                    return liveid;
                }

                public void setLiveid(String liveid) {
                    this.liveid = liveid;
                }
            }

            public static class Saicheng2Bean {
                /**
                 * c1 : 已结束
                 * c2 : 05-20周日
                 * c3 : 03:00
                 * c4T1 : 卡昂
                 * c4T1URL :
                 * c4R : 0-0
                 * c4T2 : 巴黎圣日耳曼
                 * c4T2URL :
                 * c51 : 全场统计
                 * c51Link :
                 * c52 : 全场战报
                 * c52Link :
                 * liveid : 920898
                 */

                private String c1;
                private String c2;
                private String c3;
                private String c4T1;
                private String c4T1URL;
                private String c4R;
                private String c4T2;
                private String c4T2URL;
                private String c51;
                private String c51Link;
                private String c52;
                private String c52Link;
                private String liveid;

                public String getC1() {
                    return c1;
                }

                public void setC1(String c1) {
                    this.c1 = c1;
                }

                public String getC2() {
                    return c2;
                }

                public void setC2(String c2) {
                    this.c2 = c2;
                }

                public String getC3() {
                    return c3;
                }

                public void setC3(String c3) {
                    this.c3 = c3;
                }

                public String getC4T1() {
                    return c4T1;
                }

                public void setC4T1(String c4T1) {
                    this.c4T1 = c4T1;
                }

                public String getC4T1URL() {
                    return c4T1URL;
                }

                public void setC4T1URL(String c4T1URL) {
                    this.c4T1URL = c4T1URL;
                }

                public String getC4R() {
                    return c4R;
                }

                public void setC4R(String c4R) {
                    this.c4R = c4R;
                }

                public String getC4T2() {
                    return c4T2;
                }

                public void setC4T2(String c4T2) {
                    this.c4T2 = c4T2;
                }

                public String getC4T2URL() {
                    return c4T2URL;
                }

                public void setC4T2URL(String c4T2URL) {
                    this.c4T2URL = c4T2URL;
                }

                public String getC51() {
                    return c51;
                }

                public void setC51(String c51) {
                    this.c51 = c51;
                }

                public String getC51Link() {
                    return c51Link;
                }

                public void setC51Link(String c51Link) {
                    this.c51Link = c51Link;
                }

                public String getC52() {
                    return c52;
                }

                public void setC52(String c52) {
                    this.c52 = c52;
                }

                public String getC52Link() {
                    return c52Link;
                }

                public void setC52Link(String c52Link) {
                    this.c52Link = c52Link;
                }

                public String getLiveid() {
                    return liveid;
                }

                public void setLiveid(String liveid) {
                    this.liveid = liveid;
                }
            }

            public static class JifenbangBean {
                /**
                 * c1 : 1
                 * c2 : 巴黎圣日耳曼
                 * c2L :
                 * c3 : 38
                 * c41 : 29
                 * c42 : 6
                 * c43 : 3
                 * c5 : 79
                 * c6 : 93
                 */

                private String c1;
                private String c2;
                private String c2L;
                private String c3;
                private String c41;
                private String c42;
                private String c43;
                private String c5;
                private String c6;

                public String getC1() {
                    return c1;
                }

                public void setC1(String c1) {
                    this.c1 = c1;
                }

                public String getC2() {
                    return c2;
                }

                public void setC2(String c2) {
                    this.c2 = c2;
                }

                public String getC2L() {
                    return c2L;
                }

                public void setC2L(String c2L) {
                    this.c2L = c2L;
                }

                public String getC3() {
                    return c3;
                }

                public void setC3(String c3) {
                    this.c3 = c3;
                }

                public String getC41() {
                    return c41;
                }

                public void setC41(String c41) {
                    this.c41 = c41;
                }

                public String getC42() {
                    return c42;
                }

                public void setC42(String c42) {
                    this.c42 = c42;
                }

                public String getC43() {
                    return c43;
                }

                public void setC43(String c43) {
                    this.c43 = c43;
                }

                public String getC5() {
                    return c5;
                }

                public void setC5(String c5) {
                    this.c5 = c5;
                }

                public String getC6() {
                    return c6;
                }

                public void setC6(String c6) {
                    this.c6 = c6;
                }
            }

            public static class SheshoubangBean {
                /**
                 * c1 : 1
                 * c2 : 卡瓦尼
                 * c2L :
                 * c3 : 巴黎圣日耳曼
                 * c3L :
                 * c4 : 28
                 * c5 : 3
                 */

                private String c1;
                private String c2;
                private String c2L;
                private String c3;
                private String c3L;
                private String c4;
                private String c5;

                public String getC1() {
                    return c1;
                }

                public void setC1(String c1) {
                    this.c1 = c1;
                }

                public String getC2() {
                    return c2;
                }

                public void setC2(String c2) {
                    this.c2 = c2;
                }

                public String getC2L() {
                    return c2L;
                }

                public void setC2L(String c2L) {
                    this.c2L = c2L;
                }

                public String getC3() {
                    return c3;
                }

                public void setC3(String c3) {
                    this.c3 = c3;
                }

                public String getC3L() {
                    return c3L;
                }

                public void setC3L(String c3L) {
                    this.c3L = c3L;
                }

                public String getC4() {
                    return c4;
                }

                public void setC4(String c4) {
                    this.c4 = c4;
                }

                public String getC5() {
                    return c5;
                }

                public void setC5(String c5) {
                    this.c5 = c5;
                }
            }
        }
    }
}
