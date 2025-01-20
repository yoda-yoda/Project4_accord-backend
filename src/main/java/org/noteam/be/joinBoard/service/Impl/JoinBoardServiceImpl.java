package org.noteam.be.joinBoard.service.Impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.domain.Status;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardRegisterRequest;
import org.noteam.be.joinBoard.dto.JoinBoardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardUpdateRequest;
import org.noteam.be.joinBoard.repository.JoinBoardRepository;
import org.noteam.be.joinBoard.service.JoinBoardService;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.joinBoard.JoinBoardNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinBoardServiceImpl implements JoinBoardService {

    private final JoinBoardRepository joinBoardRepository;


    // 메서드 기능: dto를 매개변수로 받아서 저장한다.
    // 반환: 저장한 엔티티를 JoinBoardResponse 로 변환하고 반환한다.
    @Transactional
    public JoinBoardResponse createJoinBoardByDto(JoinBoardRegisterRequest dto) {

        JoinBoard entity = JoinBoardRegisterRequest.toEntity(dto);

        JoinBoard savedEntity = joinBoardRepository.save(entity);

        return JoinBoardResponse.fromEntity(savedEntity);

    }


    // 메서드 기능: id로 JoinBoard 엔티티를 찾아온다.
    // 예외: 찾지못하면 예외를 던진다.
    // 반환(status = delete 여도 반환): JoinBoard 엔티티를 반환한다.
    public JoinBoard getJoinBoardEntityById(Long id) {

        return joinBoardRepository.findById(id)
                .orElseThrow( () -> new JoinBoardNotFoundException( ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR) );

    }


    // 메서드 기능: id로 JoinBoard 엔티티를 찾아온다.
    // 예외: 찾지못하면 예외를 던진다.
    // 반환(status = ACTIVE 만): 즉 딜리트처리 안된 JoinBoard 엔티티를 반환한다.
    public JoinBoard getJoinBoardEntityByIdWithNoDeleted(Long id) {

        return joinBoardRepository.findById(id)
                .filter( entity -> entity.getStatus().equals(Status.ACTIVE) )
                .orElseThrow( () -> new JoinBoardNotFoundException( ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR) );
    }


    // 메서드 기능: id로 JoinBoard 엔티티를 찾고 응답dto로 반환한다.
    // 예외: 찾지못하면 예외를 던진다.
    // 반환(status = ACTIVE 만): 딜리트처리 안된 JoinBoard 엔티티를 찾고 응답dto로 바꿔 반환한다.
    public JoinBoardResponse getJoinBoardById(Long id) {

        JoinBoard find = joinBoardRepository.findById(id)
                .filter( entity -> entity.getStatus().equals(Status.ACTIVE) )
                .orElseThrow(() -> new JoinBoardNotFoundException(ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR));

        return JoinBoardResponse.fromEntity(find);
    }


    // 메서드 기능: DB에서 글을 전부 찾아 응답으로 변환후 리스트에 담아 반환한다. 나중에 필요해질까봐 만들어둔 메서드다.
    // 예외: 없다. 즉 찾지못해도 예외없이 빈 리스트를 반환한다.
    // 반환: 응답dto들을 List에 담아 반환한다.
    // Status: ACTIVE 만 찾아온다.
    public List<JoinBoardResponse> getAllJoinBoard() {

        // 최종 반환 리스트를 미리 만듬
        List<JoinBoardResponse> responses = new ArrayList<>();

        List<JoinBoard> collect = joinBoardRepository.findAll()
                .stream()
                .filter( entity -> entity.getStatus().equals(Status.ACTIVE) )
                .collect(Collectors.toList());


        // 해당값이 없으면 빈 리스트를 반환하고 종료
        if (collect.isEmpty()) {
            return responses;
        }

        // 찾은 엔티티들이 있다면 각각 응답으로 변환후 최종 반환할 변수에 add한다.
        for (JoinBoard joinBoard : collect) {
            JoinBoardResponse joinBoardResponse = JoinBoardResponse.fromEntity(joinBoard);
            responses.add(joinBoardResponse);

        }

        return responses;

    }


    // 메서드 기능: 구인 게시판을 처음 들어갔을때 글 목록을 전부 보여주기위한 백엔드 메서드다.
    // 즉, DB에서 글을 전부 찾아 카드용 응답으로 변환후 리스트에 담아 반환한다.
    // 예외: 없다. 즉 찾지못해도 예외없이 빈 리스트를 반환한다.
    // 반환: 카드 응답 dto들을 List에 담아 반환한다.
    // Status: ACTIVE 만 찾아온다.
    public List<JoinBoardCardResponse> getAllJoinBoardCard() {

        // 최종 반환 리스트를 미리 만듬
        List<JoinBoardCardResponse> responses = new ArrayList<>();

        List<JoinBoard> collect = joinBoardRepository.findAll()
                .stream()
                .filter( entity -> entity.getStatus().equals(Status.ACTIVE) )
                .collect(Collectors.toList());


        // 해당값이 없으면 빈 리스트를 반환하고 종료
        if (collect.isEmpty()) {
            return responses;
        }

        // 찾은 엔티티들이 있다면 각각 응답으로 변환후 최종 반환할 변수에 add한다.
        for (JoinBoard joinBoard : collect) {
            JoinBoardCardResponse response = JoinBoardCardResponse.getResponseFromEntity(joinBoard);
            responses.add(response);

        }

        return responses;

    }


    // 메서드 기능: 업데이트 dto를 받아와서 기존 JoinBoard 엔티티를 수정한다.
    // 참고: 기존 엔티티를 조회할때 이곳 내부 메서드를 활용했다.
    // 예외: 내부 메서드에 예외가 내재되어있다.
    // 반환: 수정한 엔티티를 응답 dto로 바꾼뒤 반환한다.
    @Transactional
    public JoinBoardResponse updateJoinBoardById(Long id, JoinBoardUpdateRequest dto) {

        JoinBoard entity = getJoinBoardEntityByIdWithNoDeleted(id);
        entity.updateFromDto(dto);

        return JoinBoardResponse.fromEntity(entity);
    }



    // 메서드 기능: 해당 id의 JoinBoard 를 소프트 딜리트 처리한다.
    // 즉 Enum 타입인 status 변수를, DELETE로 변경한다.
    // 참고: 기존 엔티티를 조회할때 이곳 내부 메서드를 활용했다.
    // 예외: 내부 메서드에 예외가 내재되어있다.
    // 반환: X
    @Transactional
    public void deleteJoinBoardById(Long id) {

        JoinBoard entity = getJoinBoardEntityByIdWithNoDeleted(id);
        entity.changeStatus(Status.DELETE);

    }


    // 메서드 기능: 페이지 설정값을 매개변수로 받아 page 객체를 반환한다.
    // 예외: 없다. 즉 DB에 해당 값이 없다면, 빈 페이지 객체를 반환한다.
    // 반환: DB의 엔티티를 전부 JoinBoardCardResponse 라는 dto로 변환하고, 그것을 page 객체로 만들어 반환한다.
    public Page<JoinBoardCardResponse> getAllJoinBoardCardByPage(int page) {

        // 페이지 설정값(설정 객체) 생성
        Pageable pageable = PageRequest.of(page, 10);

        // 해당 설정값을 매개변수로 전달하며 동시에 ACTIVE 상태의 글을 찾아 Page 객체를 생성
        Page<JoinBoard> pagingEntity = joinBoardRepository.findByStatus(Status.ACTIVE, pageable);

        // 나중에 페이지 객체로 바꿀때 사용할 List 변수
        List<JoinBoardCardResponse> cardList = new ArrayList<>();

        // 만약 DB에 해당 값이 없다면, 비어있는 페이지 객체를 만들어서 반환
        if (  pagingEntity.getContent().isEmpty() ) {
            Page<JoinBoardCardResponse> emptyPage = new PageImpl<>(cardList, pageable, pagingEntity.getTotalElements());
            return emptyPage;
        }

        // 값이 존재한다면, 페이지 객체 내부에 있는 각각의 엔티티를 모두 Card dto로 변환하여 List 변수에 담는다
        for (JoinBoard joinBoard : pagingEntity.getContent()) {
            JoinBoardCardResponse res = JoinBoardCardResponse.getResponseFromEntity(joinBoard);
            cardList.add(res);
        }

        // PageImpl 객체의 생성자를 통해, 최종 Page 객체를 만들었다
        Page<JoinBoardCardResponse> pagingCard = new PageImpl<>(cardList, pageable, pagingEntity.getTotalElements());

        // 최종 Page 객체 반환
        return pagingCard;
    }







}
