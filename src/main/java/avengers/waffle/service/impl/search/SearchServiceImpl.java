package avengers.waffle.service.impl.search;


import avengers.waffle.VO.search.SearchVO;
import avengers.waffle.VO.util.PageVO;
import avengers.waffle.entity.Member;
import avengers.waffle.entity.MemberSearchList;
import avengers.waffle.mapper.SearchMapper;
import avengers.waffle.repository.search.MemberSearchListRepository;
import avengers.waffle.service.IF.search.IF_SearchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements IF_SearchService {

    private final SearchMapper searchMapper;
    private final RedisTemplate<String, Object> cacheRedisTemplate;
    private final MemberSearchListRepository memberSearchListRepository;

    @Override
    //@Cacheable(value = "movies", key = "#movieSearch")
    public Map<String, Object> searchMovie(SearchVO movieSearch) {

        String cacheKey = "search:movie:" + movieSearch.toString();

        Map<String, Object> data = (Map<String, Object>) cacheRedisTemplate.opsForValue().get(cacheKey);
        System.out.println(data);
        if(data!=null){
            return data;
        }
        Map<String, Object> result=new HashMap<>();
        result.put("movie", searchMapper.searchMovie(movieSearch));

        PageVO pageVO = new PageVO();
        pageVO.setPage(movieSearch.getPage());
        pageVO.setTotalCount(searchMapper.getPage(movieSearch));
        result.put("page", pageVO);

        cacheRedisTemplate.opsForValue().set(cacheKey, result, 6000, TimeUnit.MINUTES);
        return result;
    }

    //@Cacheable(value = "TV", key = "#movieSearch", )
    @Override
    public Map<String, Object> searchTV(SearchVO movieSearch) {

        String cacheKey = "search:tv:" + movieSearch.toString();
        Map<String, Object> data= (Map<String, Object>) cacheRedisTemplate.opsForValue().get(cacheKey);
        System.out.println(data);
        if(data!=null){
            return data;
        }
        Map<String, Object> result=new HashMap<>();
        result.put("movie", searchMapper.searchTV(movieSearch));

        PageVO pageVO = new PageVO();
        pageVO.setPage(movieSearch.getPage());
        pageVO.setTotalCount(searchMapper.getPageTV(movieSearch));
        result.put("page", pageVO);
        // TTL 6000분 설정
        cacheRedisTemplate.opsForValue().set(cacheKey, result, 6000, TimeUnit.MINUTES);
        return result;
    }

    @Override
    public List<String> setSerachList(String memberId,String title) {

        if(memberId==null){
            return null;
        }
        List<MemberSearchList> searchLists= memberSearchListRepository.findByMember_MemberIdOrderByIndate(memberId);
        int count=searchLists.size()-4;

        if(count>0){
            List<MemberSearchList> toDelete = searchLists.subList(0, count);
            memberSearchListRepository.deleteAllInBatch(toDelete);
        }

        memberSearchListRepository.save(MemberSearchList
                .builder()
                .member(Member.builder().memberId(memberId).build())
                .searchWord(title).build());

        return memberSearchListRepository.findByMember_MemberIdOrderByIndate(memberId).stream().map(MemberSearchList::getSearchWord).collect(Collectors.toList());
    }

    @Override
    public List<String> getSearchList(String memberId) {
        return memberSearchListRepository.findByMember_MemberIdOrderByIndate(memberId).stream().map(MemberSearchList::getSearchWord).collect(Collectors.toList());
    }

}
