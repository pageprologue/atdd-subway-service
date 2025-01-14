package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @Embedded
    private Money money;
    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, int money) {
        this.name = name;
        this.color = color;
        this.money = new Money(money);
    }

    public Line(String name, String color, int money, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.money = new Money(money);
        addSection(new Section(upStation, downStation, distance));
    }

    public void addSection(Section newSection) {
        this.sections.add(newSection);
        newSection.setLine(this);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Section> sortedSections = getSections();

        List<Station> stations = new ArrayList<>();
        stations.add(sortedSections.get(0).getUpStation());
        stations.addAll(getDownStations(sortedSections));
        return stations;
    }

    private List<Station> getDownStations(List<Section> sortedSections) {
        return sortedSections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public void removeSection(Station station) {
        sections.remove(station);
    }

    public boolean matchSection(Station source, Station target) {
        return getSections().stream()
                .anyMatch(section -> section.equalUpStation(source) && section.equalDownStation(target));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Money getMoney() {
        return money;
    }
}
