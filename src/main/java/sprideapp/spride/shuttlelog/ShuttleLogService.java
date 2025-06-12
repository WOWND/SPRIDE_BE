package sprideapp.spride.shuttlelog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sprideapp.spride.member.Member;
import sprideapp.spride.member.MemberRepository;
import sprideapp.spride.member.MemberService;
import sprideapp.spride.shuttle.Shuttle;
import sprideapp.spride.shuttle.ShuttleRepository;
import sprideapp.spride.shuttlelog.dto.ShuttleLogRequest;
import sprideapp.spride.shuttlelog.dto.ShuttleLogResponse;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShuttleLogService {
    private final ShuttleRepository shuttleRepository;
    private final ShuttleLogRepository shuttleLogRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    //단일 조회
    @Transactional(readOnly = true)
    public List<ShuttleLogResponse> getLogs(Long shuttleId) {
        Shuttle shuttle = getShuttle(shuttleId);
        List<ShuttleLog> list = shuttle.getShuttleLogs();

        return list.stream()
                .map(ShuttleLogResponse::from)
                .toList();
    }

    //로그 생성
    public ShuttleLogResponse saveLog(Long shuttleId, ShuttleLogRequest request, Long memberId) {
        Shuttle shuttle = getShuttle(shuttleId);
        Member member = getMember(memberId);
        ShuttleLog save = shuttleLogRepository.save(request.toEntity(shuttle, member));

        //활동 기록 해주기
        //memberService.saveActivity();
        return ShuttleLogResponse.from(save);
    }


    private Shuttle getShuttle(Long shuttleId) {
        return shuttleRepository.findById(shuttleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다"));
    }
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다"));
    }
}
