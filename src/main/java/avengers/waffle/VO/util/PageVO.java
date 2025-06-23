//
//package avengers.waffle.VO.util;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class PageVO {
//    private int startNo;
//    private int endNo;
//    private int perPageNum=20;
//    private Integer page; // Integer 웹에서 받은 페이지 정보(String)가 없으면 null인데 int는 null을 저장할 수 없다. 오류방지
//    private int totalCount;
//    private int endPage;
//    private int startPage;
//    private boolean prev;
//    private boolean next;
//    // 검색용 변수 2개 추가
//    private String searchType;
//    private String searchKeyword;
//
//    private void calcPage() {
//        startNo = (this.page - 1) * perPageNum +1;
//        int tempEnd = (int) (Math.ceil(page / (double) this.perPageNum) * this.perPageNum);
//        // 6/10=0.6  > 1   10
//        // 11/10=1.1  > 2   20
//
//        this.startPage = (tempEnd - this.perPageNum) + 1;
//        if (tempEnd * this.perPageNum > this.totalCount) {
//            this.endPage = (int) Math.ceil(this.totalCount / (double) this.perPageNum);
//            if(endPage!=page) {
//                this.endNo = startNo + this.perPageNum - 1;
//            }else {
//                this.endNo = this.totalCount;
//            }
//        } else {
//            this.endPage = tempEnd;
//            this.endNo = startNo + this.perPageNum - 1;
//        }
//
//        this.prev = this.startPage != 1;
//        this.next = this.endPage * this.perPageNum < this.totalCount;
//
//    }
//
//    public void setTotalCount(int totalCount) {
//        this.totalCount = totalCount;
//        calcPage();// totalCount 전제게시물개수가 있어야지 페이지계산을 할 수 있기 때문에
//    }
//
//}