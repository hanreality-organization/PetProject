package com.punuo.pet.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.member.R;
import com.punuo.pet.member.R2;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sip.dev.DevManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.DeviceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kuiya on 2019/7/30.
 */

@Route(path = MemberRouter.ROUTER_ABOUT_ACTIVITY)
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R2.id.user_agreement)
    RelativeLayout userAgreement;
    private ImageView mBack;
    private TextView mTitle;
    private TextView version;
    private ImageView mLogo;
    private int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        init();

    }

    public void init() {
        mLogo = findViewById(R.id.logo);
        mBack = findViewById(R.id.back);
        mTitle = findViewById(R.id.title);
        version = findViewById(R.id.version);

        mTitle.setText("关于我们");
        version.setText("当前版本：v" + DeviceHelper.getVersionName());
        mBack.setOnClickListener(this);
        userAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });
        ViewGroup.LayoutParams layoutParams = mLogo.getLayoutParams();
        int width = CommonUtil.getWidth() - CommonUtil.dip2px(120f);
        layoutParams.width = width;
        layoutParams.height = width * 3 / 4;
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                if (clickCount == 3) {
                    DevManager.getInstance().clearDevConfirm(AboutActivity.this, 1);
                    clickCount = 0;
                }
            }
        });
    }



    public void showAboutDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("用户协议");
        title.setPadding(10,10,10,10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.black));
        dialog.setCustomTitle(title);
        dialog.setMessage("您好！欢迎您使用梦视宠物。\n" +
                "\n" +
                "1.特别提示\n" +
                "\n" +
                "1.1 为了更好地为您提供服务，请您仔细阅读这份协议。本协议是您与本应用就您登录本应用平台进行注册及使用等所涉及的全部行为所订立的权利义务规范。您在注册过程中点击“注册”等按钮、及注册后登录和使用时，均表明您已完全同意并接受本协议，愿意遵守本协议的各项规则、规范的全部内容，若不同意则可停止注册或使用本应用平台。如您是未成年人，您还应要求您的监护人仔细阅读本协议，并取得他/他们的同意。\n" +
                "\n" +
                "1.2 为提高用户的使用感受和满意度，用户同意本应用将基于用户的操作行为对用户数据进行调查研究和分析，从而进一步优化服务。\n" +
                "\n" +
                "2.服务内容\n" +
                "\n" +
                "2.1本应用服务的具体内容由本应用制作者根据实际情况提供。\n" +
                "\n" +
                "2.2 除非本注册及服务协议另有其它明示规定，本应用所推出的新产品、新功能、新服务，均受到本注册及注册协议规范。\n" +
                "\n" +
                "2.3 本应用仅提供相关的网络服务，除此之外与相关网络服务有关的设备(如个人电脑、手机、及其他与接入互联网或移动网有关的装置)及所需的费用(如为接入互联网而支付的电话费及上网费、为使用移动网而支付的手机费)均应由用户自行负担。\n" +
                "\n" +
                "3.使用规则\n" +
                "\n" +
                "3.1 用户帐号注册\n" +
                "\n" +
                "3.1.1使用本应用系统注册的用户，只能使用汉字、英文字母、数字、下划线及它们的组合，禁止使用空格、各种符号和特殊字符，且最多不超过16个字符(8个汉字)注册，否则将不予注册。\n" +
                "\n" +
                "3.1.2使用第三方合作网站登录的用户，只能使用汉字、英文字母、数字、下划线及它们的组合，禁止使用空格、各种符号和特殊字符，且最多不超过14个字符(7个汉字)注册，否则本社区有权只截取前14个字符（7个汉字）予以显示用户帐号（若该用户帐号与应用现有用户帐号重名，系统将随机添加一个字符以示区别），否则将不予注册。\n" +
                "\n" +
                "3.2 如发现用户帐号中含有不雅文字或不恰当名称的，心动保留取消其用户资格的权利。\n" +
                "\n" +
                "3.2.1 请勿以党和国家领导人或其他社会名人的真实姓名、字号、艺名、笔名注册；\n" +
                "\n" +
                "3.2.2 请勿以国家机构或其他机构的名称注册；\n" +
                "\n" +
                "3.2.3 请勿注册不文明、不健康名字，或包含歧视、侮辱、猥亵类词语的帐号；\n" +
                "\n" +
                "3.2.4 请勿注册易产生歧义、引起他人误解或其它不符合法律规定的帐号。\n" +
                "\n" +
                "3.3 用户帐号的所有权归本应用，用户仅享有使用权。\n" +
                "\n" +
                "3.4 用户有义务保证密码和帐号的安全，用户利用该密码和帐号所进行的一切活动引起的任何损失或损害，由用户自行承担全部责任，本应用不承担任何责任。如用户发现帐号遭到未授权的使用或发生其他任何安全问题，应立即修改帐号密码并妥善保管，如有必要，请反馈通知本应用管理人员。因黑客行为或用户的保管疏忽导致帐号非法使用，本应用不承担任何责任。\n" +
                "\n" +
                "3.5 用户承诺对其发表或者上传于本应用的所有信息(即属于《中华人民共和国著作权法》规定的作品，包括但不限于文字、图片、音乐、电影、表演和录音录像制品和电脑程序等)均享有完整的知识产权，或者已经得到相关权利人的合法授权；如用户违反本条规定造成本应用被第三人索赔的，用户应全额补偿本应用的一切费用(包括但不限于各种赔偿费、诉讼代理费及为此支出的其它合理费用)；\n" +
                "\n" +
                "3.6 当第三方认为用户发表或者上传于本应用的信息侵犯其权利，并根据《信息网络传播权保护条例》或者相关法律规定向本应用发送权利通知书时，用户同意本应用可以自行判断决定删除涉嫌侵权信息，除非用户提交书面证据材料排除侵权的可能性，本应用将不会自动恢复上述删除的信息；\n" +
                "\n" +
                "(1)不得为任何非法目的而使用网络服务系统；\n" +
                "\n" +
                "(2)遵守所有与网络服务有关的网络协议、规定和程序；\n" +
                "\n" +
                "(3)不得利用本应用的服务进行任何可能对互联网的正常运转造成不利影响的行为；\n" +
                "\n" +
                "(4)不得利用本应用服务进行任何不利于本应用的行为。\n" +
                "\n" +
                "3.7 如用户在使用网络服务时违反上述任何规定，本应用有权要求用户改正或直接采取一切必要的措施(包括但不限于删除用户上传的内容、暂停或终止用户使用网络服务的权利)以减轻用户不当行为而造成的影响。\n" +
                "\n" +
                "4.责任声明\n" +
                "\n" +
                "4.1 任何网站、单位或者个人如认为本应用或者本应用提供的相关内容涉嫌侵犯其合法权益，应及时向本应用提供书面权力通知，并提供身份证明、权属证明及详细侵权情况证明。本应用在收到上述法律文件后，将会尽快切断相关内容以保证相关网站、单位或者个人的合法权益得到保障。\n" +
                "\n" +
                "4.2 用户明确同意其使用本应用网络服务所存在的风险及一切后果将完全由用户本人承担，本应用对此不承担任何责任。\n" +
                "\n" +
                "4.3 本应用无法保证网络服务一定能满足用户的要求，也不保证网络服务的及时性、安全性、准确性。\n" +
                "\n" +
                "4.4 本应用不保证为方便用户而设置的外部链接的准确性和完整性，同时，对于该等外部链接指向的不由本应用实际控制的任何网页上的内容，本应用不承担任何责任。\n" +
                "\n" +
                "5.知识产权\n" +
                "\n" +
                "5.1 本应用特有的标识、版面设计、编排方式等版权均属本应用享有，未经本应用许可授权，不得任意复制或转载。\n" +
                "\n" +
                "5.2 用户从本应用的服务中获得的信息，未经本应用的许可，不得任意复制或转载。\n" +
                "\n" +
                "5.3 本应用的所有内容，包括商品描述、图片等内容所有权归属于梦视宠物APP的用户，任何人不得转载。\n" +
                "\n" +
                "5.4 本应用所有用户上传内容仅代表用户自己的立场和观点，与本应用无关，由作者本人承担一切法律责任。\n" +
                "\n" +
                "5.5 上述及其他任何本服务包含的内容的知识产权均受到法律保护，未经本应用、用户或相关权利人书面许可，任何人不得以任何形式进行使用或创造相关衍生作品。\n" +
                "\n" +
                "6.隐私保护\n" +
                "\n" +
                "6.1 本应用不对外公开或向第三方提供单个用户的注册资料及用户在使用网络服务时存储在本社区的非公开内容，但下列情况除外：\n" +
                "\n" +
                "(1)事先获得用户的明确授权；\n" +
                "\n" +
                "(2)根据有关的法律法规要求；\n" +
                "\n" +
                "(3)按照相关政府主管部门的要求；\n" +
                "\n" +
                "(4)为维护社会公众的利益。\n" +
                "\n" +
                "6.2 本应用可能会与第三方合作向用户提供相关的网络服务，在此情况下，如该第三方同意承担与本社区同等的保护用户隐私的责任，则本社区有权将用户的注册资料等信息提供给该第三方，并无须另行告知用户。\n" +
                "\n" +
                "6.3 在不透露单个用户隐私资料的前提下，本应用有权对整个用户数据库进行分析并对用户数据库进行商业上的利用。\n" +
                "\n" +
                "7.协议修改\n" +
                "\n" +
                "7.1 本应用有权随时修改本协议的任何条款，一旦本协议的内容发生变动，本应用将会在本应用上公布修改之后的协议内容，若用户不同意上述修改，则可以选择停止使用本应用。本应用也可选择通过其他适当方式（比如系统通知）向用户通知修改内容。\n" +
                "\n" +
                "7.2 如果不同意本应用对本协议相关条款所做的修改，用户有权停止使用本应用。如果用户继续使用本应用，则视为用户接受本应用对本协议相关条款所做的修改。\n" +
                "\n" +
                "8.通知送达\n" +
                "\n" +
                "8.1 本协议项下本应用对于用户所有的通知均可通过网页公告、电子邮件、系统通知、微博管理帐号主动联系、私信、手机短信或常规的信件传送等方式进行；该等通知于发送之日视为已送达收件人。\n" +
                "\n" +
                "8.2 用户对于本应用的通知应当通过本应用对外正式公布的通信地址、电子邮件地址等联系信息进行送达。");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        }
    }
}
