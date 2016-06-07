package com.cattsoft.framework.pub;

/**
 * 所有的常量
 * 
 * @author xueweiwei
 * 
 */
public class Constant {

	// public static String
	// ServerURL="http://svr913.cattsoft.com:9961/web_mos/";//公司演示环境地址

//	public static String ServerURL = "http://10.21.1.24:8080/system-mobile/webservice/jaxrs/";// 本机地址
	public static String ServerURL = "http://124.162.56.217:7070/wonms2/"; // 重庆地址


	//public static String ServerURL = "http://113.204.227.78:9897/wonms2/"; // 重庆生产环境地址

	public static String crashURL = "tm/MosHandleFileAction.do?method=uploadFile4MOS";// 发送客户端崩溃日志的地址

	public static final int SO_TIMEOUT = 15 * 1000; // 设置等待数据超时时间30秒钟
	public static final int REQUEST_TIMEOUT = 15 * 1000;// 设置请求超时30秒钟


	// 操作系统名称
	public static final String OPERATING_SYSTEM = "Android";

	// 异常信息常量
	public static final int JSON_ANALYZE_EXCEPTION = -2; // 解析服务端返回json数据错误
	public static final int NETWORK_CONNECTION_EXCEPTION = -1; // 网络连接异常
	public static final int DESCRYPTED_OR_DECOMPRESSED_EXCEPTION = 0; // 解压或解密异常
	public static final int RETURN_DATA_SUCCESS = 1; // 服务器成功返回数据
	public static final int NO_RESPONSE_EXCEPTION = 2; // 未获得服务器响应结果
	public static final int SERVER_EXCEPTION = 3; // 服务端结果错误

	// 请求码常量 startActivityForResult()启动activity时使用
	public static final int IMAGE_REQUEST_CODE = 0;// 本地图片请求码
	public static final int CAMERA_REQUEST_CODE = 1;// 调用照相机
	public static final int CAOP_RESULT_REQUEST_CODE = 2;// 剪辑结果码
	public static final int UPLAOD_RESULT_REQUEST_CODE = 3;// 上传结果码
	public static final int CAMERA_PREVIEW_RESULT_CODE = 4;// 拍照之后预览图片码
	public static final int IMAGE_PREVIEW_RESULT_CODE = 5;// 选择本地图片后预览图片码

	// 剪切图片的比例 及 长宽
	public static final int IMAGE_CROP_WIDTH_SCALE = 3;// 和手机屏幕的比例一致
	public static final int IMAGE_CROP_HIGHT_SCALE = 5;// 和手机屏幕的比例一致
	public static final int IMAGE_CROP_WIDTH = 3 * 80;
	public static final int IMAGE_CROP_HIGHT = 5 * 80;

	// 一堆图片链接
	public static final String[] IMAGES = new String[] {
			"http://image.baidu.com/i?ct=503316480&z=&tn=baiduimagedetail&ipn=d&word=android%E5%9B%BE%E6%A0%87%E7%B4%A0%E6%9D%90&step_word=&ie=utf-8&in=17824&cl=2&lm=-1&st=-1&pn=1&rn=1&di=125665977470&ln=1998&fr=ala1&&fmq=1395716610829_R&ic=0&s=&se=1&sme=0&tab=&width=&height=&face=0&is=&istype=2&ist=&jit=&objurl=http%3A%2F%2Fpic19.nipic.com%2F20120312%2F2251064_160408797000_2.jpg#pn1&-1&di125665977470&objURLhttp%3A%2F%2Fpic19.nipic.com%2F20120312%2F2251064_160408797000_2.jpg&fromURLippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bgtrtv_z%26e3Bv54AzdH3Ffi5oAzdH3FnAzdH3FbdAzdH3Fcbdcad8hudvl91m8_z%26e3Bip4s&W452&H408&T8243&S12&TPjpg",
			"http://image.baidu.com/i?ct=503316480&z=&tn=baiduimagedetail&ipn=d&word=android%E5%9B%BE%E6%A0%87%E7%B4%A0%E6%9D%90&step_word=&ie=utf-8&in=17824&cl=2&lm=-1&st=-1&pn=1&rn=1&di=125665977470&ln=1998&fr=ala1&&fmq=1395716610829_R&ic=0&s=&se=1&sme=0&tab=&width=&height=&face=0&is=&istype=2&ist=&jit=&objurl=http%3A%2F%2Fpic19.nipic.com%2F20120312%2F2251064_160408797000_2.jpg#pn2&-1&di80584566920&objURLhttp%3A%2F%2Fwww.94it.cn%2Fuploads%2Fallimg%2F130613%2F22325934U_1.jpg&fromURLippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bl9tp_z%26e3BvgAzdH3FwAzdH3F3tg2x7wgk5hjAzdH3Fda8nAzdH3Fam8nAzdH3F9amba_z%26e3Bip4s&W502&H438&T8696&S76&TPjpg",
			"http://image.baidu.com/i?ct=503316480&z=&tn=baiduimagedetail&ipn=d&word=android%E5%9B%BE%E6%A0%87%E7%B4%A0%E6%9D%90&step_word=&ie=utf-8&in=17824&cl=2&lm=-1&st=-1&pn=1&rn=1&di=125665977470&ln=1998&fr=ala1&&fmq=1395716610829_R&ic=0&s=&se=1&sme=0&tab=&width=&height=&face=0&is=&istype=2&ist=&jit=&objurl=http%3A%2F%2Fpic19.nipic.com%2F20120312%2F2251064_160408797000_2.jpg#pn3&-1&di71007193070&objURLhttp%3A%2F%2Fimg.download.pchome.net%2F42%2F1n%2F215008_800x600.jpg&fromURLippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bi5g2xtgj17_z%26e3B562AzdH3Ft4w2jfAzdH3FDjuw7spAzdH3F%3Fr%3D%25Ba%25Bd%25D0%25BF%25CD%25Bb%25Cn%25F0%25CD%25BC%25B8%25EA%25CB%25Db%25Bd%25C9_z%26e3Bip4s&W512&H512&T8672&S18&TPjpg",
			"http://image.baidu.com/i?ct=503316480&z=&tn=baiduimagedetail&ipn=d&word=android%E5%9B%BE%E6%A0%87%E7%B4%A0%E6%9D%90&step_word=&ie=utf-8&in=17824&cl=2&lm=-1&st=-1&pn=1&rn=1&di=125665977470&ln=1998&fr=ala1&&fmq=1395716610829_R&ic=0&s=&se=1&sme=0&tab=&width=&height=&face=0&is=&istype=2&ist=&jit=&objurl=http%3A%2F%2Fpic19.nipic.com%2F20120312%2F2251064_160408797000_2.jpg#pn7&-1&di49760788000&objURLhttp%3A%2F%2Fwww.uml.org.cn%2Fjmshj%2Fimages%2F2012122755.png&fromURLippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B74s_z%26e3B562_z%26e3BvgAzdH3F34fi3AzdH3Fda8d8dd0c_z%26e3Bwfr&W450&H260&T8365&S28&TPpng",
			"http://image.baidu.com/i?ct=503316480&z=&tn=baiduimagedetail&ipn=d&word=android%E5%9B%BE%E6%A0%87%E7%B4%A0%E6%9D%90&step_word=&ie=utf-8&in=17824&cl=2&lm=-1&st=-1&pn=1&rn=1&di=125665977470&ln=1998&fr=ala1&&fmq=1395716610829_R&ic=0&s=&se=1&sme=0&tab=&width=&height=&face=0&is=&istype=2&ist=&jit=&objurl=http%3A%2F%2Fpic19.nipic.com%2F20120312%2F2251064_160408797000_2.jpg#pn25&-1&di72425551790&objURLhttp%3A%2F%2Fb.hiphotos.bdimg.com%2Falbum%2Fw%253D2048%2Fsign%3D18c847f0b21bb0518f24b4280242d8b4%2Ff603918fa0ec08fae082587558ee3d6d54fbda66.jpg&fromURLippr_z2C%24qAzdH3FAzdH3Ft4w2j_z%26e3Bkwt17_z%26e3Bv54AzdH3F1jpwtsAzdH3Ftg1jx%3Frtvp76j_t1%3Dlc0a0am0la%26u654%3Da%267fj6_t1%3Dbmlb9bbac%26wsk74_t1%3Dn999m9dnm&W400&H300&T13753&S36&TPjpg" };
	public static final int SCANNIN_GREQUEST_CODE = 1;// 调用扫描界面码
}
