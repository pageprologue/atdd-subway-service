package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    private Distance distance;

    public Section() {
    }

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public boolean isNotDuplicate(Section section) {
        if (isDuplicate(section)) {
            throw new IllegalArgumentException("같은 상행역과 하행역으로 등록된 구간이 이미 존재합니다.");
        }

        return isTerminusExtend(section) || isBetweenStations(section);
    }

    public Section divide(Section newSection) {
        if (isBetweenStations(newSection) && distance.divisible(newSection)) {
            changeStationLink(newSection);
            distance = distance.minusDistance(newSection.distance);
        }
        return newSection;
    }

    private void changeStationLink(Section newSection) {
        if (upStation.equals(newSection.upStation)) {
            upStation = newSection.downStation;
        }

        if (downStation.equals(newSection.downStation)) {
            downStation = newSection.upStation;
        }
    }

    private boolean isDuplicate(Section section) {
        return upStation.equals(section.upStation) && downStation.equals(section.downStation);
    }

    private boolean isTerminusExtend(Section section) {
        return upStation.equals(section.downStation) || downStation.equals(section.upStation);
    }

    public boolean isBetweenStations(Section section) {
        return upStation.equals(section.upStation) || downStation.equals(section.downStation);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
